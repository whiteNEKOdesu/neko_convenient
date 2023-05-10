package neko.convenient.nekoconvenientorder8008.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单生成记录表，用于解锁已取消订单库存
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_log")
public class OrderLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String orderLogId;

    private String orderRecord;

    private String uid;

    /**
     * 收货地址id
     */
    private String receiveAddressId;

    /**
     * 订单价格
     */
    private BigDecimal cost;

    /**
     * -1->取消，0->未支付，1->已支付
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
