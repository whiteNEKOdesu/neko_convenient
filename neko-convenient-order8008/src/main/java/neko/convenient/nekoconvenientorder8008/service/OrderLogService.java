package neko.convenient.nekoconvenientorder8008.service;

import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientorder8008.vo.PreOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;

import java.util.List;

/**
 * <p>
 * 订单生成记录表，用于解锁已取消订单库存 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface OrderLogService extends IService<OrderLog> {
    String preOrder(PreOrderVo vo);

    List<ProductInfoVo> getPreOrderProductInfos(String orderRecord);

    void newOrderLogService(OrderLog orderLog);

    OrderLog getOrderLogByOrderRecord(String orderRecord);

    void updateOrderLogStatusToCancel(String orderRecord);
}
