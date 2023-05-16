package neko.convenient.nekoconvenientware8007.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
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
@Data
@Accessors(chain = true)
@TableName("stock_lock_log")
public class StockLockLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String stockLockLogId;

    /**
     * 订单生成记录id，对应order_log表order_record
     */
    private String orderRecord;

    /**
     * 库存id
     */
    private String wareId;

    private String skuId;

    /**
     * 库存锁定数量
     */
    private Integer lockNumber;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * -1->已取消锁定，0->锁定中，1->用户已支付
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
