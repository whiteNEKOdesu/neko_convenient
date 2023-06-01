package neko.convenient.nekoconvenientware8007.config;

import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    /**
     * 使用JSON序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 库存交换机
     */
    @Bean
    public TopicExchange stockExchange(){
        return ExchangeBuilder.topicExchange(RabbitMqConstant.STOCK_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 库存解锁队列
     */
    @Bean
    public Queue stockReleaseStockQueue(){
        return QueueBuilder.durable(RabbitMqConstant.STOCK_RELEASE_QUEUE_NAME)
                .build();
    }

    /**
     * 库存解锁死信队列
     */
    @Bean
    public Queue stockReleaseDelayQueue(){
        return QueueBuilder.durable(RabbitMqConstant.STOCK_RELEASE_DELAY_QUEUE_NAME)
                .deadLetterExchange(RabbitMqConstant.STOCK_EXCHANGE_NAME)
                .deadLetterRoutingKey(RabbitMqConstant.STOCK_RELEASE_QUEUE_ROUTING_KEY_NAME)
                .ttl(1000 * 60 * 5)
                .build();
    }

    /**
     * 库存扣减队列
     */
    @Bean
    public Queue stockDecreaseQueue(){
        return QueueBuilder.durable(RabbitMqConstant.STOCK_DECREASE_QUEUE_NAME)
                .build();
    }

    /**
     * 库存解锁队列跟库存交换机绑定
     */
    @Bean
    public Binding stockReleaseBinding(Queue stockReleaseStockQueue, TopicExchange stockExchange){
        return BindingBuilder.bind(stockReleaseStockQueue)
                .to(stockExchange)
                .with(RabbitMqConstant.STOCK_RELEASE_QUEUE_ROUTING_KEY_NAME);
    }

    /**
     * 库存解锁死信队列跟库存交换机绑定
     */
    @Bean
    public Binding stockLockedBinding(Queue stockReleaseDelayQueue, TopicExchange stockExchange){
        return BindingBuilder.bind(stockReleaseDelayQueue)
                .to(stockExchange)
                //此routingKey不会发送给任何消费者，只为实现延迟队列
                .with(RabbitMqConstant.STOCK_DEAD_LETTER_ROUTING_KEY_NAME);
    }

    /**
     * 库存扣减队列跟库存交换机绑定
     */
    @Bean
    public Binding stockDecreaseBinding(Queue stockDecreaseQueue, TopicExchange stockExchange){
        return BindingBuilder.bind(stockDecreaseQueue)
                .to(stockExchange)
                .with(RabbitMqConstant.STOCK_DECREASE_QUEUE_ROTING_KEY_NAME);
    }
}
