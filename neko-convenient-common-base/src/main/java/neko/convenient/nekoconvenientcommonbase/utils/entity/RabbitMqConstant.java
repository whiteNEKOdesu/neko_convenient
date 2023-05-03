package neko.convenient.nekoconvenientcommonbase.utils.entity;

public class RabbitMqConstant {
    //库存交换机名
    public static final String STOCK_EXCHANGE_NAME = "neko-convenient-stock-exchange";

    //库存解锁队列名
    public static final String STOCK_RELEASE_QUEUE_NAME = "neko.convenient.stock.release.queue";

    //库存解锁延迟队列名
    public static final String STOCK_RELEASE_DELAY_QUEUE_NAME = "neko.convenient.stock.release.delay.queue";

    //库存解锁队列routingKey名
    public static final String STOCK_RELEASE_QUEUE_ROUTING_KEY_NAME = "neko.convenient.stock.release.#";

    //库存死信队列routingKey名
    public static final String STOCK_DEAD_LETTER_ROUTING_KEY_NAME = "neko.convenient.stock.release";
}
