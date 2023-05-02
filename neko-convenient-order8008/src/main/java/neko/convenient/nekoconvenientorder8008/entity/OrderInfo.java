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

    private String uid;

    /**
     * 收货地址id
     */
    private String receiveAddressId;

    private String skuId;

    private String skuName;

    private String skuImage;

    private String spuId;

    private String brandId;

    private String brandName;

    private String marketId;

    /**
     * 订单购买数量
     */
    private Integer number;

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
     * 快递员id
     */
    private String deliverWorkerId;

    /**
     * 快递员名
     */
    private String deliverWorkerName;

    /**
     * 订单类型，0->正常购买，1->团购
     */
    private Byte type;

    /**
     * 0->已支付，1->配送中，2->完成
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
