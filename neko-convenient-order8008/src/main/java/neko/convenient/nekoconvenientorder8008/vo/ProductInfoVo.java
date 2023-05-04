package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProductInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skuId;

    private String spuId;

    private String marketId;

    /**
     * 商店所属品牌id
     */
    private String brandId;

    private Integer skuNumber;

    /**
     * 商店名
     */
    private String brandName;

    private String skuName;

    private String skuImage;

    private BigDecimal price;
}
