package neko.convenient.nekoconvenientware8007.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrderLogTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderLogId;

    private String orderRecord;

    private String uid;

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
