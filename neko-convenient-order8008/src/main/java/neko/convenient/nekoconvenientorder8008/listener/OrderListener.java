package neko.convenient.nekoconvenientorder8008.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.StockUnlockException;
import neko.convenient.nekoconvenientorder8008.feign.ware.WareInfoFeignService;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@RabbitListener(queues = RabbitMqConstant.STOCK_RELEASE_QUEUE_NAME)
@Slf4j
public class OrderListener {
    @Resource
    private OrderLogService orderLogService;

    @Resource
    private WareInfoFeignService wareInfoFeignService;

    @RabbitHandler
    public void StockUnlock(String orderRecord, Message message, Channel channel) throws IOException {
        log.info("订单超时准备验证关闭，订单号: " + orderRecord);

        try {
            //修改订单状态为取消状态
            orderLogService.updateOrderLogStatusToCancel(orderRecord);

            //远程调用库存微服务解锁库存
            log.info("订单超时远程调用库存微服务解锁库存，订单号: " + orderRecord);
            ResultObject<Object> r = wareInfoFeignService.unlockStock(orderRecord);
            if(!r.getResponseCode().equals(200)){
                throw new StockUnlockException("库存解锁异常");
            }

            //单个确认消费消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            log.error("订单关闭发生异常，订单号: " + orderRecord);
            //拒收消息，并让消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}