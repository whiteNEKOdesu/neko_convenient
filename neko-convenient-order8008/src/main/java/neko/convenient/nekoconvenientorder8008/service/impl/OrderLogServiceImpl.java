package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ProductServiceException;
import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import neko.convenient.nekoconvenientorder8008.feign.product.SkuInfoFeignService;
import neko.convenient.nekoconvenientorder8008.mapper.OrderLogMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientorder8008.vo.PreOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单生成记录表，用于解锁已取消订单库存 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SkuInfoFeignService skuInfoFeignService;

    /**
     * 预生成订单，生成 token 保证接口幂等性
     */
    @Override
    public String preOrder(PreOrderVo vo) {
        //生成 token 保证接口幂等性，并可以用来作为订单号
        String orderRecord = IdWorker.getTimeId();
        String key = Constant.ORDER_REDIS_PREFIX + "order_record:" + StpUtil.getLoginId().toString() + orderRecord;
        List<PreOrderVo.PreOrderProductInfoVo> productInfos = vo.getProductInfos();

        //获取所有skuId
        List<String> skuIds = productInfos.stream().filter(Objects::nonNull)
                .map(PreOrderVo.PreOrderProductInfoVo::getSkuId)
                .collect(Collectors.toList());

        //远程调用product微服务获取 sku 信息
        ResultObject<List<ProductInfoVo>> r = skuInfoFeignService.productInfos(skuIds);
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }
        List<ProductInfoVo> result = r.getResult();

        //设置商品价格
        List<ProductInfoVo> productInfoVos = result.stream().peek(productInfoVo -> {
            for (PreOrderVo.PreOrderProductInfoVo preOrderProductInfoVo : productInfos) {
                if (preOrderProductInfoVo.getSkuId().equals(productInfoVo.getSkuId())) {
                    productInfoVo.setSkuNumber(preOrderProductInfoVo.getSkuNumber());
                    productInfoVo.setPrice(productInfoVo.getPrice().multiply(new BigDecimal(preOrderProductInfoVo.getSkuNumber() + "")));
                }
            }
        }).collect(Collectors.toList());

        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(productInfoVos),
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);
        //从购物车提交，记录保证在订单完成后删除购物车物品
        if(vo.getIsFromPurchaseList()){
            stringRedisTemplate.opsForValue().setIfAbsent(key + ":is_from_purchase_list",
                    "true",
                    1000 * 60 * 10,
                    TimeUnit.MILLISECONDS);
        }

        return orderRecord;
    }

    /**
     * 获取预生成订单信息
     */
    @Override
    public List<ProductInfoVo> getPreOrderProductInfos(String orderRecord) {
        String key = Constant.ORDER_REDIS_PREFIX + "order_record:" + StpUtil.getLoginId().toString() + orderRecord;
        String preOrder = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.hasText(preOrder)){
            throw new NoSuchResultException("无此预生成订单信息");
        }

        return JSONUtil.toList(JSONUtil.parseArray(preOrder), ProductInfoVo.class);
    }

    /**
     * 新增订单生成记录
     */
    @Override
    public void newOrderLogService(OrderLog orderLog) {
        LocalDateTime now = LocalDateTime.now();
        orderLog.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(orderLog);
    }

    /**
     * 根据订单号获取获取预生成订单状态
     */
    @Override
    public OrderLog getOrderLogByOrderRecord(String orderRecord) {
        return this.baseMapper.selectOne(new QueryWrapper<OrderLog>().lambda()
                .eq(OrderLog::getOrderRecord, orderRecord));
    }

    /**
     * 根据订单号修改订单状态为取消状态
     */
    @Override
    public void updateOrderLogStatusToCancel(String orderRecord) {
        this.baseMapper.updateOrderLogStatusToCancel(orderRecord, LocalDateTime.now());
    }

    /**
     * 向购物车中添加商品
     */
    @Override
    public void addSkusIntoPurchaseList(PreOrderVo vo) {
        String key = Constant.ORDER_REDIS_PREFIX + "purchase_list:" + StpUtil.getLoginId().toString();
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(key);
        Long size = stringObjectObjectBoundHashOperations.size();
        //购物车最大存放 50 件商品
        if(size != null && size > 50){
            throw new NotPermissionException("权限不足");
        }
        List<PreOrderVo.PreOrderProductInfoVo> productInfos = vo.getProductInfos();

        //获取所有skuId
        List<String> skuIds = productInfos.stream().filter(Objects::nonNull)
                .map(PreOrderVo.PreOrderProductInfoVo::getSkuId)
                .collect(Collectors.toList());

        //远程调用product微服务获取 sku 信息
        ResultObject<List<ProductInfoVo>> r = skuInfoFeignService.productInfos(skuIds);
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }
        List<ProductInfoVo> result = r.getResult();

        result.forEach(productInfoVo -> {
            for (PreOrderVo.PreOrderProductInfoVo preOrderProductInfoVo : productInfos) {
                if (preOrderProductInfoVo.getSkuId().equals(productInfoVo.getSkuId())) {
                    Boolean isAppend = stringObjectObjectBoundHashOperations.hasKey(productInfoVo.getSkuId());
                    //如果 key 存在，追加购物车商品数量
                    if(isAppend != null && isAppend){
                        String value = (String) stringObjectObjectBoundHashOperations.get(productInfoVo.getSkuId());
                        ProductInfoVo todoProductInfo = JSONUtil.toBean(value, ProductInfoVo.class);
                        productInfoVo.setSkuNumber(preOrderProductInfoVo.getSkuNumber() + todoProductInfo.getSkuNumber());
                        productInfoVo.setPrice(productInfoVo.getPrice().multiply(new BigDecimal(productInfoVo.getSkuNumber() + "")));
                    }else{
                        productInfoVo.setSkuNumber(preOrderProductInfoVo.getSkuNumber());
                        productInfoVo.setPrice(productInfoVo.getPrice().multiply(new BigDecimal(preOrderProductInfoVo.getSkuNumber() + "")));
                    }
                    stringObjectObjectBoundHashOperations.put(productInfoVo.getSkuId(), JSONUtil.toJsonStr(productInfoVo));
                }
            }
        });
    }

    /**
     * 获取购物车信息
     */
    @Override
    public List<ProductInfoVo> getPurchaseListInfo() {
        String key = Constant.ORDER_REDIS_PREFIX + "purchase_list:" + StpUtil.getLoginId().toString();
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(key);
        Set<Object> keys = stringObjectObjectBoundHashOperations.keys();
        if(keys == null || keys.isEmpty()){
            return Collections.emptyList();
        }

        List<ProductInfoVo> purchaseList = new ArrayList<>();
        for(Object todoKey : keys){
            String value = (String) stringObjectObjectBoundHashOperations.get(todoKey);
            purchaseList.add(JSONUtil.toBean(value, ProductInfoVo.class));
        }

        return purchaseList;
    }

    /**
     * 删除指定skuId购物车信息
     */
    @Override
    public void deletePurchaseList(List<String> skuIds, String key) {
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(key);
        for(String todoKey : skuIds){
            stringObjectObjectBoundHashOperations.delete(todoKey);
        }
    }
}
