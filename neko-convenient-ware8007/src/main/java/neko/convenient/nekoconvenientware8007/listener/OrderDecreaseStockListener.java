package neko.convenient.nekoconvenientware8007.listener;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientware8007.service.WareInfoService;
import neko.convenient.nekoconvenientware8007.to.RabbitMQOrderMessageTo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@RabbitListener(queues = RabbitMqConstant.STOCK_DECREASE_QUEUE_NAME)
@Slf4j
public class OrderDecreaseStockListener {
    @Resource
    private WareInfoService wareInfoService;

    @RabbitHandler
    public void confirmLockStockPay(String jsonMessage, Message message, Channel channel) throws IOException {
        RabbitMQOrderMessageTo rabbitMQOrderMessageTo = JSONUtil.toBean(jsonMessage, RabbitMQOrderMessageTo.class);
        String orderRecord = rabbitMQOrderMessageTo.getOrderRecord();
        try {
            wareInfoService.confirmLockStockPay(orderRecord);
            //单个确认消费消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            log.error("解锁指定订单号库存并扣除库存发生异常，订单号: " + orderRecord);
            //拒收消息，并让消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
