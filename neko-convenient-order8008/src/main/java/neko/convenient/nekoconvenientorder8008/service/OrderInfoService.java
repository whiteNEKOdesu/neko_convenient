package neko.convenient.nekoconvenientorder8008.service;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientorder8008.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface OrderInfoService extends IService<OrderInfo> {
    void newOrder(NewOrderVo vo) throws ExecutionException, InterruptedException;

    String getAlipayPayPage(String orderRecord, String token);

    Boolean isOrderAvailable(String orderRecord);

    List<ProductInfoVo> getAvailableOrderInfos(String orderRecord);

    String alipayTradeCheck(AliPayAsyncVo vo, HttpServletRequest request) throws AlipayApiException;

    OrderInfo getOrderInfoByOrderRecord(String orderRecord);

    Page<OrderInfo> getUserSelfOrderInfoByQueryLimitedPage(QueryVo vo);

    Page<CourierOrderInfoVo> getUnpickOrderInfoByQueryLimitedPage(QueryVo vo);

    void pickOrder(CourierPickOrderVo vo);

    void courierConfirmDelivered(String orderId);

    void userConfirmDelivered(String orderId);

    Page<CourierOrderInfoVo> getUserSelfPickOrderInfoByQueryLimitedPage(QueryVo vo);
}
