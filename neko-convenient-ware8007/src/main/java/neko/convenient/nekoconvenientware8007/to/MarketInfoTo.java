package neko.convenient.nekoconvenientware8007.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MarketInfoTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String marketId;

    /**
     * 开店人uid
     */
    private String uid;

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
     * 开店许可证url
     */
    private String certificateUrl;

    /**
     * 地址根id，对应address_dict表addres_id
     */
    private Integer addressId;

    /**
     * 地址
     */
    private String address;

    private Boolean isBan;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
