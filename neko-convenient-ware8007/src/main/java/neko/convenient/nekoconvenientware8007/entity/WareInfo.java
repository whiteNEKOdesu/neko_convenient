package neko.convenient.nekoconvenientware8007.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Getter
@Setter
@Accessors(chain = true)
@TableName("ware_info")
public class WareInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String wareId;

    private String marketId;

    private String skuId;

    private Integer stock;

    /**
     * 锁定库存数量
     */
    private Integer lock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
