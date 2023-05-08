package neko.convenient.nekoconvenientorder8008.service.impl;

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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
}
