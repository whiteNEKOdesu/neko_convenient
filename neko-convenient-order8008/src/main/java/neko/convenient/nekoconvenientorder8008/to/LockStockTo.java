package neko.convenient.nekoconvenientorder8008.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LockStockTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String marketId;

    private String skuId;

    /**
     * 订单生成记录id，对应order_log表order_record
     */
    private String orderRecord;

    /**
     * 库存锁定数量
     */
    private Integer lockNumber;
}
