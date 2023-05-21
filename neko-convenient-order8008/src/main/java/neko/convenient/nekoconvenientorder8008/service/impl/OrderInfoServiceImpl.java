package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.*;
import neko.convenient.nekoconvenientcommonbase.utils.exception.*;
import neko.convenient.nekoconvenientorder8008.config.AliPayTemplate;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;
import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import neko.convenient.nekoconvenientorder8008.feign.member.MemberLevelDictFeignService;
import neko.convenient.nekoconvenientorder8008.feign.product.PointDictFeignService;
import neko.convenient.nekoconvenientorder8008.feign.product.SkuInfoFeignService;
import neko.convenient.nekoconvenientorder8008.feign.ware.WareInfoFeignService;
import neko.convenient.nekoconvenientorder8008.mapper.OrderInfoMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderDetailInfoService;
import neko.convenient.nekoconvenientorder8008.service.OrderInfoService;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
import neko.convenient.nekoconvenientorder8008.to.AliPayTo;
import neko.convenient.nekoconvenientorder8008.to.LockStockTo;
import neko.convenient.nekoconvenientorder8008.to.MemberLevelTo;
import neko.convenient.nekoconvenientorder8008.to.OrderRedisTo;
import neko.convenient.nekoconvenientorder8008.vo.AliPayAsyncVo;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private OrderLogService orderLogService;

    @Resource
    private OrderDetailInfoService orderDetailInfoService;

    @Resource
    private WareInfoFeignService wareInfoFeignService;

    @Resource
    private SkuInfoFeignService skuInfoFeignService;

    @Resource
    private MemberLevelDictFeignService memberLevelDictFeignService;

    @Resource
    private PointDictFeignService pointDictFeignService;

    @Resource
    private AliPayTemplate aliPayTemplate;

    @Resource
    private Executor threadPool;

    /**
     * 新增订单
     */
    @Override
    public void newOrder(NewOrderVo vo) throws ExecutionException, InterruptedException {
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

        //锁定库存 to
        LockStockTo lockStockTo = new LockStockTo();
        List<LockStockTo.LockInfo> lockInfos = new ArrayList<>();
        lockStockTo.setOrderRecord(orderRecord)
                .setLockInfos(lockInfos);

        CompletableFuture<BigDecimal> totalPriceTask = CompletableFuture.supplyAsync(() -> {
            //总价
            BigDecimal totalPrice = new BigDecimal("0");
            for (ProductInfoVo productInfo : productInfos) {
                //计算总价
                totalPrice = totalPrice.add(productInfo.getPrice());

                //为锁定库存 to 设置信息
                LockStockTo.LockInfo lockInfo = new LockStockTo.LockInfo();
                lockInfo.setMarketId(productInfo.getMarketId())
                        .setSkuId(productInfo.getSkuId())
                        .setLockNumber(productInfo.getSkuNumber())
                        .setPrice(productInfo.getPrice());
                lockInfos.add(lockInfo);
            }

            return totalPrice;
        }, threadPool);

        CompletableFuture<Void> orderLogTask = totalPriceTask.thenAcceptAsync((totalPrice) -> {
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
        }, threadPool);

        String orderKey = Constant.ORDER_REDIS_PREFIX + "order:" + uid + orderRecord;
        CompletableFuture<Void> orderRedisTask = CompletableFuture.runAsync(() -> {
            OrderRedisTo orderRedisTo = new OrderRedisTo();
            orderRedisTo.setReceiveAddressId(vo.getReceiveAddressId())
                    .setOrderRecord(orderRecord)
                    .setProductInfos(productInfos);
            //将订单详情信息存入 redis
            stringRedisTemplate.opsForValue().setIfAbsent(orderKey,
                    JSONUtil.toJsonStr(orderRedisTo),
                    1000 * 60 * 4,
                    TimeUnit.MILLISECONDS);
        }, threadPool);

        CompletableFuture<Void> alipayTask = totalPriceTask.thenAcceptAsync((totalPrice) -> {
            AliPayTo aliPayTo = new AliPayTo();
            //远程调用member微服务获取会员等级折扣信息
            ResultObject<MemberLevelTo> memberLevelDictResult = memberLevelDictFeignService.userLevelInfo(uid);
            if (!memberLevelDictResult.getResponseCode().equals(200)) {
                throw new MemberServiceException("member微服务远程调用异常");
            }
            MemberLevelTo memberLevelTo = memberLevelDictResult.getResult();
            //为折扣价格设置精度
            BigDecimal actualPrice = totalPrice.multiply(new BigDecimal(memberLevelTo.getDiscount() * 0.01 + ""))
                    .setScale(2, BigDecimal.ROUND_DOWN);
            aliPayTo.setOut_trade_no(orderRecord)
                    .setSubject("NEKO_CONVENIENT")
                    //设置折扣价格
                    .setTotal_amount(actualPrice.toString())
                    .setBody("NEKO_CONVENIENT");
            String alipayPageKey = orderKey + ":pay_page";
            //将支付宝支付页面信息存入 redis
            try {
                stringRedisTemplate.opsForValue().setIfAbsent(alipayPageKey,
                        aliPayTemplate.pay(aliPayTo),
                        1000 * 60 * 4,
                        TimeUnit.MILLISECONDS);
            }catch (Exception e){
                e.printStackTrace();
            }
        }, threadPool);

        CompletableFuture<Void> lockStockTask = totalPriceTask.thenRunAsync(() -> {
            //远程调用库存微服务锁定库存
            ResultObject<Object> r = wareInfoFeignService.lockStock(lockStockTo);
            if (!r.getResponseCode().equals(200)) {
                throw new StockNotEnoughException("库存不足");
            }
        }, threadPool);

        CompletableFuture.allOf(orderLogTask, orderRedisTask, alipayTask, lockStockTask).get();
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
     * 根据订单号查询已创建订单商品详情信息
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

    /**
     * 支付宝异步支付通知处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
                if(orderLog == null){
                    log.error("订单号: " + vo.getOut_trade_no() + "，支付宝流水号: " + vo.getTrade_no() + "，订单不存在");
                    return "error";
                }

                //远程调用product微服务获取积分信息
                ResultObject<Integer> pointDictResult = pointDictFeignService.pricePoint(orderLog.getCost());
                if(!pointDictResult.getResponseCode().equals(200)){
                    throw new ProductServiceException("product微服务远程调用异常");
                }
                Integer point = pointDictResult.getResult();

                //生成订单信息
                String uid = orderLog.getUid();
                OrderInfo orderInfo = new OrderInfo();
                LocalDateTime now = LocalDateTime.now();
                orderInfo.setOrderRecord(vo.getOut_trade_no())
                        .setAlipayTradeId(vo.getTrade_no())
                        .setUid(uid)
                        .setReceiveAddressId(orderLog.getReceiveAddressId())
                        .setCost(orderLog.getCost())
                        .setActualCost(new BigDecimal(vo.getInvoice_amount()))
                        .setPoint(point)
                        .setCreateTime(now)
                        .setUpdateTime(now);

                OrderLog todoOrderLog = new OrderLog();
                todoOrderLog.setOrderLogId(orderLog.getOrderLogId())
                        .setStatus(PreorderStatus.PAY);
                //更新订单生成记录为已支付状态
                orderLogService.updateById(todoOrderLog);

                //记录订单
                this.baseMapper.insert(orderInfo);

                //远程调用product微服务获取订单详情信息
                ResultObject<List<OrderDetailInfo>> r = skuInfoFeignService.orderDetailInfos(vo.getOut_trade_no());
                if(!r.getResponseCode().equals(200)){
                    throw new ProductServiceException("商品微服务远程调用异常");
                }

                List<OrderDetailInfo> result = r.getResult();
                for(OrderDetailInfo orderDetailInfo : result){
                    orderDetailInfo.setUid(uid)
                            .setCreateTime(now)
                            .setUpdateTime(now);
                }

                //记录订单详情信息
                orderDetailInfoService.saveBatch(result);

                //远程调用ware微服务解锁指定订单号库存并扣除库存
                ResultObject<Object> confirmLockStockPayResult = wareInfoFeignService.confirmLockStockPay(vo.getOut_trade_no());
                if(!confirmLockStockPayResult.getResponseCode().equals(200)){
                    throw new WareServiceException("库存微服务远程调用异常");
                }
                String key = Constant.ORDER_REDIS_PREFIX + "order_record:" + uid + orderInfo.getOrderRecord() + ":is_from_purchase_list";
                String purchaseListKey = Constant.ORDER_REDIS_PREFIX + "purchase_list:" + uid;
                String isFromPurchase = stringRedisTemplate.opsForValue().get(key);
                //删除购物车已购买商品
                if(isFromPurchase != null){
                    List<String> skuIds = result.stream().map(OrderDetailInfo::getSkuId)
                            .collect(Collectors.toList());
                    orderLogService.deletePurchaseList(skuIds, purchaseListKey);
                }

                log.info("订单号: " + vo.getOut_trade_no() + "，支付宝流水号: " + vo.getTrade_no() + "，订单支付确认完成");
            }

            return "success";
        }else{
            return "error";
        }
    }

    /**
     * 根据订单号获取订单信息
     */
    @Override
    public OrderInfo getOrderInfoByOrderRecord(String orderRecord) {
        OrderInfo orderInfo = this.baseMapper.selectOne(new QueryWrapper<OrderInfo>()
                .lambda()
                .eq(OrderInfo::getOrderRecord, orderRecord));
        if(!orderInfo.getUid().equals(StpUtil.getLoginId().toString())){
            throw new NotPermissionException("权限不足");
        }

        return orderInfo;
    }

    /**
     * 分页查询用户自身订单信息
     */
    @Override
    public Page<OrderInfo> getUserSelfOrderInfoByQueryLimitedPage(QueryVo vo) {
        Page<OrderInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderInfo::getUid, StpUtil.getLoginId().toString())
                .orderByDesc(OrderInfo::getOrderId);
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(OrderInfo::getOrderRecord, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
