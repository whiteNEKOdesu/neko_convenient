package neko.convenient.nekoconvenientorder8008.service;

import com.alipay.api.AlipayApiException;
import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientorder8008.vo.AliPayAsyncVo;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface OrderInfoService extends IService<OrderInfo> {
    void newOrder(NewOrderVo vo) throws AlipayApiException;

    String getAlipayPayPage(String orderRecord, String token);

    Boolean isOrderAvailable(String orderRecord);

    List<ProductInfoVo> getAvailableOrderInfos(String orderRecord);

    String alipayTradeCheck(AliPayAsyncVo vo, HttpServletRequest request) throws AlipayApiException;
}
