package neko.convenient.nekoconvenientmember8003.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 快递员申请表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("courier_apply_info")
public class CourierApplyInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String applyId;

    /**
     * 申请人uid
     */
    private String uid;

    private String idCardNumber;

    private String idCardImage;

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
