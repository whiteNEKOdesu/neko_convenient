package neko.convenient.nekoconvenientware8007.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Getter
@Setter
@Accessors(chain = true)
@TableName("stock_update_log")
public class StockUpdateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stockUpdateLogId;

    /**
     * 库存操作人uid
     */
    private String uid;

    /**
     * 库存操作人用户名
     */
    private String userName;

    /**
     * 更新数量
     */
    private Integer updateNumber;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
