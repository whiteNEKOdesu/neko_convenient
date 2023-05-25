package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CourierOrderInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;

    private String orderRecord;

    private String uid;

    /**
     * 收货地址id
     */
    private String receiveAddressId;

    /**
     * 订单类型，0->正常购买，1->团购
     */
    private Byte type;

    /**
     * 0->已支付，1->配送中，2->快递员送达，3->完成
     */
    private Byte status;

    private Integer addressId;

    /**
     * 收货地址
     */
    private String addressName;

    /**
     * 货物数量
     */
    private Integer number;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
