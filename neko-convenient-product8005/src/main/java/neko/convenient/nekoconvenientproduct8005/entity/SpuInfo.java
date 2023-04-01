package neko.convenient.nekoconvenientproduct8005.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * spu信息表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("spu_info")
public class SpuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String spuId;

    private String spuName;

    private String spuDescription;

    private String spuImage;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 商店id
     */
    private Integer marketId;

    private Boolean isPublish;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
