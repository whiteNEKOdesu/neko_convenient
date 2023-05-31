package neko.convenient.nekoconvenientorder8008.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
import neko.convenient.nekoconvenientorder8008.vo.AddPurchaseListVo;
import neko.convenient.nekoconvenientorder8008.vo.PreOrderVo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单生成记录表，用于解锁已取消订单库存 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@RestController
@RequestMapping("order_log")
public class OrderLogController {
    @Resource
    private OrderLogService orderLogService;

    /**
     * 生成 token 保证预生成订单接口幂等性
     */
    @SaCheckLogin
    @PutMapping("preorder_token")
    public ResultObject<String> preorderToken(@Validated @RequestBody PreOrderVo vo){
        return ResultObject.ok(orderLogService.preOrder(vo));
    }

    /**
     * 根据订单号获取获取预生成订单状态，建议只提供给微服务远程调用
     */
    @PostMapping("preorder_status")
    public ResultObject<OrderLog> preorderStatus(@RequestParam String orderRecord){
        return ResultObject.ok(orderLogService.getOrderLogByOrderRecord(orderRecord));
    }

    /**
     * 向购物车中添加商品
     */
    @SaCheckLogin
    @PutMapping("add_purchase_list")
    public ResultObject<Object> addPurchaseList(@Validated @RequestBody AddPurchaseListVo vo){
        orderLogService.addSkusIntoPurchaseList(vo);

        return ResultObject.ok();
    }

    /**
     * 获取购物车信息
     */
    @SaCheckLogin
    @PostMapping("purchase_list_infos")
    public ResultObject<List<ProductInfoVo>> purchaseListInfos(){
        return ResultObject.ok(orderLogService.getPurchaseListInfo());
    }
}
