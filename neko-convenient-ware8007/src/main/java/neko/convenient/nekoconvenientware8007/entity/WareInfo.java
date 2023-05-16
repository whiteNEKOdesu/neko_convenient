package neko.convenient.nekoconvenientware8007.entity;

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
 * sku库存信息表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Data
@Accessors(chain = true)
@TableName("ware_info")
public class WareInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String wareId;

    private String marketId;

    private String skuId;

    private Integer stock;

    /**
     * 锁定库存数量
     */
    private Integer lockNumber;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
