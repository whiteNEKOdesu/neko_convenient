package neko.convenient.nekoconvenientorder8008.scheduled;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.MQMessageType;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientorder8008.entity.MQReturnMessage;
import neko.convenient.nekoconvenientorder8008.service.MQReturnMessageService;
import neko.convenient.nekoconvenientorder8008.to.RabbitMQOrderMessageTo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@EnableScheduling
@EnableAsync
@Slf4j
public class RabbitMQReturnMessageScheduled {
    @Resource
    private MQReturnMessageService mqReturnMessageService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "* 0 * * * ?")
    @Async
    public void resendMessage(){
        List<MQReturnMessage> list = mqReturnMessageService.lambdaQuery()
                .eq(MQReturnMessage::getIsDelete, false)
                .list();
        int size = list.size();

        for(MQReturnMessage mqReturnMessage : list){
            if(mqReturnMessage.getType().equals(MQMessageType.UNLOCK_STOCK)){
                RabbitMQOrderMessageTo rabbitMQOrderMessageTo = new RabbitMQOrderMessageTo();
                rabbitMQOrderMessageTo.setOrderRecord(mqReturnMessage.getOrderRecord())
                        .setType(MQMessageType.UNLOCK_STOCK);
                //在CorrelationData中设置回退消息
                CorrelationData correlationData = new CorrelationData(MQMessageType.UNLOCK_STOCK.toString());
                String jsonMessage = JSONUtil.toJsonStr(rabbitMQOrderMessageTo);
                String notAvailable = "not available";
                correlationData.setReturned(new ReturnedMessage(new Message(jsonMessage.getBytes(StandardCharsets.UTF_8)),
                        0,
                        notAvailable,
                        notAvailable,
                        notAvailable));
                //向延迟队列发送订单号，用于超时解锁库存
                rabbitTemplate.convertAndSend(RabbitMqConstant.STOCK_EXCHANGE_NAME,
                        RabbitMqConstant.STOCK_DEAD_LETTER_ROUTING_KEY_NAME,
                        jsonMessage,
                        correlationData);
            }else if(mqReturnMessage.getType().equals(MQMessageType.DECREASE_STOCK)){
                RabbitMQOrderMessageTo rabbitMQOrderMessageTo = new RabbitMQOrderMessageTo();
                rabbitMQOrderMessageTo.setOrderRecord(mqReturnMessage.getOrderRecord())
                        .setType(MQMessageType.DECREASE_STOCK);
                //在CorrelationData中设置回退消息
                CorrelationData correlationData = new CorrelationData(MQMessageType.DECREASE_STOCK.toString());
                String jsonMessage = JSONUtil.toJsonStr(rabbitMQOrderMessageTo);
                String notAvailable = "not available";
                correlationData.setReturned(new ReturnedMessage(new Message(jsonMessage.getBytes(StandardCharsets.UTF_8)),
                        0,
                        notAvailable,
                        notAvailable,
                        notAvailable));
                //向库存扣减队列发送消息扣除库存
                rabbitTemplate.convertAndSend(RabbitMqConstant.STOCK_EXCHANGE_NAME,
                        RabbitMqConstant.STOCK_DECREASE_QUEUE_ROTING_KEY_NAME,
                        jsonMessage,
                        correlationData);
            }
        }
    }
}
