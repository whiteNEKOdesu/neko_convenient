package neko.convenient.nekoconvenientorder8008.service;

import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface OrderInfoService extends IService<OrderInfo> {
    void newOrder(NewOrderVo vo);
}
