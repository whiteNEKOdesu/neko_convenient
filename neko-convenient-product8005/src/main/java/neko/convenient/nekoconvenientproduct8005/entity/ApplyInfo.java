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
 * 品牌申请表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("apply_info")
public class ApplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String applyId;

    /**
     * 申请人uid
     */
    private String uid;

    /**
     * 申请注册品牌名
     */
    private String brandName;

    /**
     * 申请注册品牌描述
     */
    private String brandDescription;

    /**
     * 公司证书url
     */
    private String certificateUrl;

    /**
     * 申请人身份证url
     */
    private String idCardUrl;

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
