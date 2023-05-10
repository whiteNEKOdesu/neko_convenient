package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.PreorderStatus;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.OrderOverTimeException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.StockNotEnoughException;
import neko.convenient.nekoconvenientorder8008.config.AliPayTemplate;
import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import neko.convenient.nekoconvenientorder8008.feign.ware.WareInfoFeignService;
import neko.convenient.nekoconvenientorder8008.mapper.OrderInfoMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
import neko.convenient.nekoconvenientorder8008.to.AliPayTo;
import neko.convenient.nekoconvenientorder8008.to.LockStockTo;
import neko.convenient.nekoconvenientorder8008.to.OrderRedisTo;
import neko.convenient.nekoconvenientorder8008.vo.AliPayAsyncVo;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Resource
    private AliPayTemplate aliPayTemplate;

    /**
     * 新增订单
     */
    @Override
    public void newOrder(NewOrderVo vo) throws AlipayApiException {
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
                .setReceiveAddressId(vo.getReceiveAddressId())
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
                1000 * 60 * 4,
                TimeUnit.MILLISECONDS);

        AliPayTo aliPayTo = new AliPayTo();
        aliPayTo.setOut_trade_no(orderRecord)
                .setSubject("NEKO_CONVENIENT")
                .setTotal_amount(totalPrice.toString())
                .setBody("NEKO_CONVENIENT");
        String alipayPageKey = orderKey + ":pay_page";
        //将支付宝支付页面信息存入 redis
        stringRedisTemplate.opsForValue().setIfAbsent(alipayPageKey,
                aliPayTemplate.pay(aliPayTo),
                1000 * 60 * 4,
                TimeUnit.MILLISECONDS);

        //远程调用库存微服务锁定库存
        ResultObject<Object> r = wareInfoFeignService.lockStock(lockStockTo);
        if(!r.getResponseCode().equals(200)){
            throw new StockNotEnoughException("库存不足");
        }
    }

    /**
     * 根据订单号获取支付宝支付页面
     */
    @Override
    public String getAlipayPayPage(String orderRecord, String token) {
        OrderLog orderLog = orderLogService.getOrderLogByOrderRecord(orderRecord);
        if(orderLog == null || !orderLog.getStatus().equals(PreorderStatus.UNPAY)){
            throw new OrderOverTimeException("订单超时");
        }

        String key = Constant.ORDER_REDIS_PREFIX + "order:" + StpUtil.getLoginIdByToken(token).toString() + orderRecord + ":pay_page";
        String alipayPayPage = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.hasText(alipayPayPage)){
            throw new OrderOverTimeException("订单超时");
        }

        return alipayPayPage;
    }

    /**
     * 根据订单号查询订单是否可以支付
     */
    @Override
    public Boolean isOrderAvailable(String orderRecord) {
        OrderLog orderLog = orderLogService.getOrderLogByOrderRecord(orderRecord);
        return orderLog != null && orderLog.getStatus().equals(PreorderStatus.UNPAY) &&
                orderLog.getUid().equals(StpUtil.getLoginId().toString());
    }

    /**
     * 根据订单号查询已创建订单信息
     */
    @Override
    public List<ProductInfoVo> getAvailableOrderInfos(String orderRecord) {
        String orderKey = Constant.ORDER_REDIS_PREFIX + "order:" + StpUtil.getLoginId().toString() + orderRecord;
        String orderInfo = stringRedisTemplate.opsForValue().get(orderKey);
        if(!StringUtils.hasText(orderInfo)){
            throw new OrderOverTimeException("订单超时");
        }

        return JSONUtil.toBean(orderInfo, OrderRedisTo.class).getProductInfos();
    }

    @Override
    public String alipayTradeCheck(AliPayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        //验签
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                aliPayTemplate.getAlipayPublicKey(),
                aliPayTemplate.getCharset(),
                aliPayTemplate.getSignType());

        if(signVerified){
            if(vo.getTrade_status().equals("TRADE_SUCCESS") || vo.getTrade_status().equals("TRADE_FINISHED")){
                OrderLog orderLog = orderLogService.getOrderLogByOrderRecord(vo.getOut_trade_no());

                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderRecord(vo.getOut_trade_no())
                        .setAlipayTradeId(vo.getTrade_no())
                        .setUid(orderLog.getUid())
                        .setReceiveAddressId(orderLog.getReceiveAddressId());

                this.baseMapper.insert(orderInfo);
            }

            return "success";
        }else{
            return "error";
        }
    }
}
