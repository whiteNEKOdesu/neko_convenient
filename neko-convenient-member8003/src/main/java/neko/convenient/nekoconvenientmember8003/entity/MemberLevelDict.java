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
 * 用户等级字典表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("member_level_dict")
public class MemberLevelDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "member_level_id", type = IdType.AUTO)
    private Integer memberLevelId;

    /**
     * 用户等级
     */
    private Integer memberLevel;

    /**
     * 等级名
     */
    private String levelName;

    /**
     * 达到此等级最低需要积分
     */
    private Integer achievePoint;

    /**
     * 成长积分
     */
    private Integer growPoint;

    /**
     * 折扣百分比
     */
    private Double discount;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
