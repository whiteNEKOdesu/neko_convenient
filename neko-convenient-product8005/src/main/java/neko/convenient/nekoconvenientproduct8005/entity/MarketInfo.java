package neko.convenient.nekoconvenientproduct8005.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商店信息表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("market_info")
public class MarketInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
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

    private Boolean isBan;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
