package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import neko.convenient.nekoconvenientorder8008.mapper.OrderInfoMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
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

        BigDecimal totalPrice = new BigDecimal("0");
        //计算总价
        for(ProductInfoVo productInfo : productInfos){
            totalPrice = totalPrice.add(productInfo.getPrice());
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setOrderRecord(orderRecord)
                .setUid(uid)
                .setCost(totalPrice);
        //向延迟队列发送订单号，用于超时解锁库存
        rabbitTemplate.convertAndSend(RabbitMqConstant.STOCK_EXCHANGE_NAME,
                RabbitMqConstant.STOCK_DEAD_LETTER_ROUTING_KEY_NAME,
                orderRecord);

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
    }
}
