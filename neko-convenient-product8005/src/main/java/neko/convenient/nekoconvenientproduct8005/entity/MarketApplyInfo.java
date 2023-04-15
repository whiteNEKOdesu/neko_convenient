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
 * 商店开店申请表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("market_apply_info")
public class MarketApplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String applyId;

    /**
     * 申请人uid
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
     * 商店详细地址
     */
    private String marketAddressDescription;

    /**
     * 地址根id，对应address_dict表address_id
     */
    private Integer addressId;

    /**
     * 地址
     */
    private String address;

    /**
     * 商店许可证url
     */
    private String certificateUrl;

    /**
     * 申请人身份证url
     */
    private String idCartUrl;

    /**
     * 审核管理员id
     */
    private String applyAdminId;

    /**
     * -1->正在审核，0->未通过，1->通过
     */
    private Byte status;

    private String statusInfo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
