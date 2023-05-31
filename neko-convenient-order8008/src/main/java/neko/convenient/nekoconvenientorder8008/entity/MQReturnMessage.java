package neko.convenient.nekoconvenientorder8008.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单rabbitmq消息发送失败记录表
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Data
@Accessors(chain = true)
@TableName("mq_return_message")
public class MQReturnMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String returnOrderId;

    private String orderRecord;

    /**
     * 消息类型，0->解锁库存延迟队列消息，1->支付确认扣减库存消息
     */
    private Byte type;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
