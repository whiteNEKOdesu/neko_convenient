package neko.convenient.nekoconvenientorder8008.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientorder8008.service.OrderLogService;
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
     * 预生成订单，生成 token 保证接口幂等性
     */
    @PutMapping("preorder_token")
    public ResultObject<String> preorderToken(@Validated @RequestBody PreOrderVo vo){
        return ResultObject.ok(orderLogService.preOrder(vo));
    }

    /**
     * 获取预生成订单信息
     */
    @PostMapping("preorder_info")
    public ResultObject<List<ProductInfoVo>> preorderInfo(@RequestParam String orderRecord){
        return ResultObject.ok(orderLogService.getPreOrderProductInfos(orderRecord));
    }
}
