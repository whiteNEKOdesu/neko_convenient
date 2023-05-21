package neko.convenient.nekoconvenientorder8008.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MemberLevelTo implements Serializable {
    private static final long serialVersionUID = 1L;

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
