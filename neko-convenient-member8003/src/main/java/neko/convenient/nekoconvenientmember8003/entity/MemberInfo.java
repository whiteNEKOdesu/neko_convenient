package neko.convenient.nekoconvenientmember8003.entity;

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
 * 用户信息表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("member_info")
public class MemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String uid;

    private String userName;

    private String userPassword;

    private String salt;

    /**
     * 社交登录名
     */
    private String sourceName;

    private String userImagePath;

    private Byte gender;

    /**
     * 社交登录来源
     */
    private String source;

    private String sourceUid;

    private String realName;

    private String idCardNumber;

    private String idCardImage;

    private String phone;

    private String mail;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 用户积分
     */
    private Integer point;

    private Boolean isBan;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
