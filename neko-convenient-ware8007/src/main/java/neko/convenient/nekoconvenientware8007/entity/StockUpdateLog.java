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
 * 库存更新日志表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Data
@Accessors(chain = true)
@TableName("stock_update_log")
public class StockUpdateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String stockUpdateLogId;

    /**
     * 库存操作人uid
     */
    private String uid;

    /**
     * 更新数量
     */
    private Integer updateNumber;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
