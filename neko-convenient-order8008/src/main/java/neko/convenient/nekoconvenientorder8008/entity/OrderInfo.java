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
 * 订单表
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    private String orderRecord;

    /**
     * 支付宝流水id
     */
    private String alipayTradeId;

    private String uid;

    /**
     * 收货地址id
     */
    private String receiveAddressId;

    /**
     * 订单总价
     */
    private BigDecimal cost;

    /**
     * 实际支付订单总价
     */
    private BigDecimal actualCost;

    /**
     * 获取积分
     */
    private Integer point;

    /**
     * 订单类型，0->正常购买，1->团购
     */
    private Byte type;

    /**
     * 快递员id
     */
    private String courierId;

    /**
     * 0->已支付，1->配送中，2->快递员送达，3->完成
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
