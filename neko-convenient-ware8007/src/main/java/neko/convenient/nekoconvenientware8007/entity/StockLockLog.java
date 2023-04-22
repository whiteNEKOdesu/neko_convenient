package neko.convenient.nekoconvenientware8007.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存锁定日志表
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("stock_lock_log")
public class StockLockLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stockLockLogId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 库存id
     */
    private String wareId;

    /**
     * 库存锁定数量
     */
    private Integer stock;

    /**
     * -1->已取消锁定，0->锁定中，1->用户已支付
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}