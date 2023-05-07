package neko.convenient.nekoconvenientorder8008.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class LockStockTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单生成记录id，对应order_log表order_record
     */
    private String orderRecord;

    private List<LockInfo> lockInfos;

    @Data
    @Accessors(chain = true)
    public static class LockInfo{
        private String marketId;

        private String skuId;

        /**
         * 库存锁定数量
         */
        private Integer lockNumber;
    }
}
