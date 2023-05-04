package neko.convenient.nekoconvenientware8007.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LockStockVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String marketId;

    @NotBlank
    private String skuId;

    /**
     * 订单生成记录id，对应order_log表order_record
     */
    @NotBlank
    private String orderRecord;

    /**
     * 库存锁定数量
     */
    @NotNull
    private Integer lockNumber;
}
