package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class SpuAndSkuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<SkuInfoVo> skuInfos;

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

    private String brandId;

    /**
     * 商店名
     */
    private String brandName;

    /**
     * logo图片url
     */
    private String brandLogo;

    /**
     * 商店id
     */
    private String marketId;

    /**
     * 商店详细地址
     */
    private String marketAddressDescription;

    /**
     * 开店许可证url
     */
    private String certificateUrl;
}
