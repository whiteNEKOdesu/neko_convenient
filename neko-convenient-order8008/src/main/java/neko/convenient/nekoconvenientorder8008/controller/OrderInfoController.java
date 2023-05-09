package neko.convenient.nekoconvenientorder8008.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alipay.api.AlipayApiException;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientorder8008.service.OrderInfoService;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@RestController
@RequestMapping("order_info")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 新增订单
     */
    @SaCheckLogin
    @PutMapping("new_order")
    public ResultObject<Object> newOrder(@Validated @RequestBody NewOrderVo vo) throws AlipayApiException {
        orderInfoService.newOrder(vo);

        return ResultObject.ok();
    }

    /**
     * 根据订单号获取支付宝支付页面
     */
    @GetMapping(value = "alipay_page", produces = "text/html")
    public String alipayPage(@RequestParam String orderRecord, @RequestParam String token){
        return orderInfoService.getAlipayPayPage(orderRecord, token);
    }

    /**
     * 根据订单号查询订单是否可以支付
     */
    @SaCheckLogin
    @PostMapping("is_order_available")
    public ResultObject<Boolean> isOrderAvailable(@RequestParam String orderRecord){
        return ResultObject.ok(orderInfoService.isOrderAvailable(orderRecord));
    }

    /**
     * 根据订单号查询已创建订单信息
     */
    @SaCheckLogin
    @PostMapping("available_order_infos")
    public ResultObject<List<ProductInfoVo>> availableOrderInfos(@RequestParam String orderRecord){
        return ResultObject.ok(orderInfoService.getAvailableOrderInfos(orderRecord));
    }
}
