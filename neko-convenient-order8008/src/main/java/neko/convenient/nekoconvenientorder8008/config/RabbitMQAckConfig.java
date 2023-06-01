package neko.convenient.nekoconvenientorder8008.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.MQMessageType;
import neko.convenient.nekoconvenientcommonbase.utils.exception.RabbitMQSendException;
import neko.convenient.nekoconvenientorder8008.entity.MQReturnMessage;
import neko.convenient.nekoconvenientorder8008.service.MQReturnMessageService;
import neko.convenient.nekoconvenientorder8008.to.RabbitMQOrderMessageTo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Configuration
@Slf4j
public class RabbitMQAckConfig {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MQReturnMessageService mqReturnMessageService;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(!ack){
                if(correlationData != null && correlationData.getReturned() != null){
                    String message = new String(correlationData.getReturned().getMessage().getBody(), StandardCharsets.UTF_8);
                    RabbitMQOrderMessageTo rabbitMQOrderMessageTo = JSONUtil.toBean(message, RabbitMQOrderMessageTo.class);
                    String orderRecord = rabbitMQOrderMessageTo.getOrderRecord();
                    MQReturnMessage mqReturnMessage = new MQReturnMessage();
                    if(MQMessageType.UNLOCK_STOCK.equals(rabbitMQOrderMessageTo.getType())){
                        LocalDateTime now = LocalDateTime.now();
                        mqReturnMessage.setOrderRecord(orderRecord)
                                .setType(MQMessageType.UNLOCK_STOCK)
                                .setCreateTime(now)
                                .setUpdateTime(now);
                        //记录消息
                        mqReturnMessageService.save(mqReturnMessage);

                        log.error("解锁库存延迟队列消息发送至交换机失败，订单id: " + orderRecord + "，原因: " + cause);
                    }else if(MQMessageType.DECREASE_STOCK.equals(rabbitMQOrderMessageTo.getType())){
                        LocalDateTime now = LocalDateTime.now();
                        mqReturnMessage.setOrderRecord(orderRecord)
                                .setType(MQMessageType.DECREASE_STOCK)
                                .setCreateTime(now)
                                .setUpdateTime(now);
                        //记录消息
                        mqReturnMessageService.save(mqReturnMessage);

                        log.error("支付确认扣减库存消息发送至交换机失败，订单id: " + orderRecord + "，原因: " + cause);
                    }
                }
                throw new RabbitMQSendException("消息发送失败");
            }
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            String message = new String(returnedMessage.getMessage().getBody(), StandardCharsets.UTF_8);
            RabbitMQOrderMessageTo rabbitMQOrderMessageTo = JSONUtil.toBean(message, RabbitMQOrderMessageTo.class);
            String orderRecord = rabbitMQOrderMessageTo.getOrderRecord();
            MQReturnMessage mqReturnMessage = new MQReturnMessage();
            if(MQMessageType.UNLOCK_STOCK.equals(rabbitMQOrderMessageTo.getType())){
                LocalDateTime now = LocalDateTime.now();
                mqReturnMessage.setOrderRecord(orderRecord)
                        .setType(MQMessageType.UNLOCK_STOCK)
                        .setCreateTime(now)
                        .setUpdateTime(now);
                //记录消息
                mqReturnMessageService.save(mqReturnMessage);

                log.error("解锁库存延迟队列消息被交换机: " + returnedMessage.getExchange() + " 回退，订单id: "
                        + orderRecord + "，replyCode: " +
                        returnedMessage.getReplyCode() + "，replyText: " +
                        returnedMessage.getReplyText() + "，routingKey: " +
                        returnedMessage.getRoutingKey());
            }else if(MQMessageType.DECREASE_STOCK.equals(rabbitMQOrderMessageTo.getType())){
                LocalDateTime now = LocalDateTime.now();
                mqReturnMessage.setOrderRecord(orderRecord)
                        .setType(MQMessageType.DECREASE_STOCK)
                        .setCreateTime(now)
                        .setUpdateTime(now);
                //记录消息
                mqReturnMessageService.save(mqReturnMessage);

                log.error("支付确认扣减库存消息被交换机: " + returnedMessage.getExchange() + " 回退，订单id: "
                        + orderRecord + "，replyCode: " +
                        returnedMessage.getReplyCode() + "，replyText: " +
                        returnedMessage.getReplyText() + "，routingKey: " +
                        returnedMessage.getRoutingKey());
            }
        });
    }
}
