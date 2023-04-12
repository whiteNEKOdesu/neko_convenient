package neko.convenient.nekoconvenientproduct8005.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品轮播图表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Data
@Accessors(chain = true)
@TableName("roll_spu")
public class RollSpu implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "roll_id", type = IdType.AUTO)
    private Integer rollId;

    private String rollName;

    private String spuId;

    private String spuName;

    private String spuImage;

    /**
     * 排序字段，越小越靠前
     */
    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
