package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
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

import javax.annotation.Resource;
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
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(vo),
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
        PreOrderVo preOrderVo = JSONUtil.toBean(preOrder, PreOrderVo.class);
        List<PreOrderVo.PreOrderProductInfoVo> preOrderProductInfoVos = preOrderVo.getProductInfos();
        List<String> skuIds = preOrderProductInfoVos.stream().filter(Objects::nonNull)
                .map(PreOrderVo.PreOrderProductInfoVo::getSkuId)
                .collect(Collectors.toList());

        ResultObject<List<ProductInfoVo>> r = skuInfoFeignService.productInfos(skuIds);
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }
        List<ProductInfoVo> result = r.getResult();
        return result.stream().peek(productInfoVo -> {
            for(PreOrderVo.PreOrderProductInfoVo preOrderProductInfoVo : preOrderProductInfoVos){
                if(preOrderProductInfoVo.getSkuId().equals(productInfoVo.getSkuId())){
                    productInfoVo.setSkuNumber(preOrderProductInfoVo.getSkuNumber());
                }
            }
        }).collect(Collectors.toList());
    }
}
