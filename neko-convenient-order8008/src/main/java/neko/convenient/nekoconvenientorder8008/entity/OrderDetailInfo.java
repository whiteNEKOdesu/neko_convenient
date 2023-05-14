package neko.convenient.nekoconvenientorder8008.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单详情表
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Data
@Accessors(chain = true)
@TableName("order_detail_info")
public class OrderDetailInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderRecord;

    private String skuId;

    private String skuName;

    private String skuImage;

    private String spuId;

    private String brandId;

    private String brandName;

    private String marketId;

    /**
     * 商品购买数量
     */
    private Integer number;

    /**
     * 商品总价
     */
    private BigDecimal cost;

    /**
     * 快递员id
     */
    private String deliverWorkerId;

    /**
     * 快递员名
     */
    private String deliverWorkerName;

    /**
     * 0->揽货中，1->配送中，2->完成
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
