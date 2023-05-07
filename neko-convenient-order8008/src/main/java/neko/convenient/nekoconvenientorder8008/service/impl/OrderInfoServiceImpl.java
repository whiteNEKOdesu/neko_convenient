package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.StockNotEnoughException;
import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import neko.convenient.nekoconvenientorder8008.feign.ware.WareInfoFeignService;
import neko.convenient.nekoconvenientorder8008.mapper.OrderInfoMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
import neko.convenient.nekoconvenientorder8008.to.LockStockTo;
import neko.convenient.nekoconvenientorder8008.to.OrderRedisTo;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private OrderLogService orderLogService;

    @Resource
    private WareInfoFeignService wareInfoFeignService;

    /**
     * 新增订单
     */
    @Override
    public void newOrder(NewOrderVo vo) {
        String orderRecord = vo.getOrderRecord();
        String uid = StpUtil.getLoginId().toString();
        String key = Constant.ORDER_REDIS_PREFIX + "order_record:" + uid + orderRecord;
        String preOrder = stringRedisTemplate.opsForValue().get(key);
        //删除订单 token
        Boolean isDelete = stringRedisTemplate.delete(key);
        if(isDelete == null || !isDelete || !StringUtils.hasText(preOrder)){
            throw new NoSuchResultException("无此预生成订单信息");
        }

        List<ProductInfoVo> productInfos = JSONUtil.toList(JSONUtil.parseArray(preOrder), ProductInfoVo.class);
        if(productInfos.isEmpty()){
            throw new NoSuchResultException("无此预生成订单信息");
        }

        //总价
        BigDecimal totalPrice = new BigDecimal("0");
        //锁定库存 to
        LockStockTo lockStockTo = new LockStockTo();
        List<LockStockTo.LockInfo> lockInfos = new ArrayList<>();
        lockStockTo.setOrderRecord(orderRecord)
                .setLockInfos(lockInfos);
        for(ProductInfoVo productInfo : productInfos){
            //计算总价
            totalPrice = totalPrice.add(productInfo.getPrice());

            //为锁定库存 to 设置信息
            LockStockTo.LockInfo lockInfo = new LockStockTo.LockInfo();
            lockInfo.setMarketId(productInfo.getMarketId())
                    .setSkuId(productInfo.getSkuId())
                    .setLockNumber(productInfo.getSkuNumber());
            lockInfos.add(lockInfo);
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setOrderRecord(orderRecord)
                .setUid(uid)
                .setCost(totalPrice);
        //向延迟队列发送订单号，用于超时解锁库存
        rabbitTemplate.convertAndSend(RabbitMqConstant.STOCK_EXCHANGE_NAME,
                RabbitMqConstant.STOCK_DEAD_LETTER_ROUTING_KEY_NAME,
                orderRecord,
                new CorrelationData(orderRecord));

        //新增订单生成记录，用于超时解锁库存
        orderLogService.newOrderLogService(orderLog);

        OrderRedisTo orderRedisTo = new OrderRedisTo();
        orderRedisTo.setReceiveAddressId(vo.getReceiveAddressId())
                .setOrderRecord(orderRecord)
                .setProductInfos(productInfos);
        String orderKey = Constant.ORDER_REDIS_PREFIX + "order:" + uid + orderRecord;
        //将订单详情信息存入 redis
        stringRedisTemplate.opsForValue().setIfAbsent(orderKey,
                JSONUtil.toJsonStr(orderRedisTo),
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);

        //远程调用库存微服务锁定库存
        ResultObject<Object> r = wareInfoFeignService.lockStock(lockStockTo);
        if(!r.getResponseCode().equals(200)){
            throw new StockNotEnoughException("库存不足");
        }
    }
}
