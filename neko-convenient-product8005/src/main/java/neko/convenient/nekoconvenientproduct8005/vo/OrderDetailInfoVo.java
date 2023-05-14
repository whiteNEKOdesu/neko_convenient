package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OrderDetailInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderRecord;

    private String skuId;

    private String skuName;

    private String skuImage;

    private String spuId;

    private String brandId;

    private String brandName;

    private String marketId;

    /**
     * 商品购买数量
     */
    private Integer number;

    /**
     * 商品总价
     */
    private BigDecimal cost;
}
