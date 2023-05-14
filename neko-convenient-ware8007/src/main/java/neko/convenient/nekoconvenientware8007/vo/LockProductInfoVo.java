package neko.convenient.nekoconvenientware8007.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class LockProductInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skuId;

    /**
     * 库存锁定数量
     */
    private Integer lockNumber;

    /**
     * 价格
     */
    private BigDecimal price;
}
