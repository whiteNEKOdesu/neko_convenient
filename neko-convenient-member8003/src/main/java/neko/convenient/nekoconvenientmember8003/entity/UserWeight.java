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
 * 权限表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("user_weight")
public class UserWeight implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "weight_id", type = IdType.AUTO)
    private Integer weightId;

    /**
     * 权限类型
     */
    private String weightType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
