package neko.convenient.nekoconvenientproduct8005.elasticsearch.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProductInfoES implements Serializable {
    private static final long serialVersionUID = 1L;

    private String spuId;

    private String spuName;

    private String spuDescription;

    private String spuImage;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 分类名，冗余字段
     */
    private String categoryName;

    private String skuId;

    private String skuName;

    private String skuImage;

    private BigDecimal price;

    /**
     * 商店id
     */
    private String marketId;

    /**
     * 商店所属品牌id
     */
    private String brandId;

    /**
     * 冗余字段
     */
    private String brandName;

    /**
     * 冗余字段
     */
    private String brandLogo;

    /**
     * 商店详细地址
     */
    private String marketAddressDescription;

    /**
     * 地址根id，对应address_dict表addres_id
     */
    private Integer addressId;

    /**
     * 地址
     */
    private String address;
}
