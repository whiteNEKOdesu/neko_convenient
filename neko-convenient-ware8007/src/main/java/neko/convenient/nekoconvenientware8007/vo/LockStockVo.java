package neko.convenient.nekoconvenientware8007.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class LockStockVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单生成记录id，对应order_log表order_record
     */
    @NotBlank
    private String orderRecord;

    @Valid
    @NotEmpty
    private List<LockInfo> lockInfos;

    @Data
    @Accessors(chain = true)
    public static class LockInfo{
        @NotBlank
        private String marketId;

        @NotBlank
        private String skuId;

        /**
         * 库存锁定数量
         */
        @NotNull
        private Integer lockNumber;

        /**
         * 价格
         */
        @NotNull
        private BigDecimal price;
    }
}
