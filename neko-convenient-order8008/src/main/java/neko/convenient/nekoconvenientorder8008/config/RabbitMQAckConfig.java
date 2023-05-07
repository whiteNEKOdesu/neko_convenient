package neko.convenient.nekoconvenientorder8008.config;

import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.exception.RabbitMQSendException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class RabbitMQAckConfig {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(!ack){
                if(correlationData != null){
                    log.error("消息发送至交换机失败，订单id: " + correlationData.getId() + "，原因: " + cause);
                }
                throw new RabbitMQSendException("消息发送失败");
            }
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.error("消息 " + new String(returnedMessage.getMessage().getBody(), StandardCharsets.UTF_8) +
                    " 被交换机 " + returnedMessage.getExchange() + " 回退，replyCode " +
                    returnedMessage.getReplyCode() + "，replyText " +
                    returnedMessage.getReplyText() + "，routingKey " +
                    returnedMessage.getRoutingKey());
        });
    }
}
