package neko.convenient.nekoconvenientproduct8005.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 积分字典表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("point_dict")
public class PointDict implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer pointId;

    /**
     * 获得积分最低价格
     */
    private BigDecimal minPrice;

    /**
     * 获得积分最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 获得积分
     */
    private Integer point;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
