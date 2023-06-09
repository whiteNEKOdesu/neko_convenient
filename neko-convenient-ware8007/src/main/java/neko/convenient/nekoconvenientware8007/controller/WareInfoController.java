package neko.convenient.nekoconvenientware8007.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientware8007.service.WareInfoService;
import neko.convenient.nekoconvenientware8007.vo.AddStockNumberVo;
import neko.convenient.nekoconvenientware8007.vo.LockStockVo;
import neko.convenient.nekoconvenientware8007.vo.WareInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * sku库存信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@RestController
@RequestMapping("ware_info")
public class WareInfoController {
    @Resource
    private WareInfoService wareInfoService;

    /**
     * 获取指定skuId库存信息
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @GetMapping("sku_ware_info")
    public ResultObject<WareInfoVo> skuWareInfo(@RequestParam String skuId){
        return ResultObject.ok(wareInfoService.getWareInfoVoBySkuId(skuId));
    }

    /**
     * 添加库存数量
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PostMapping("add_stock_number")
    public ResultObject<Object> addStock(@Validated @RequestBody AddStockNumberVo vo){
        wareInfoService.addStockNumber(vo);

        return ResultObject.ok();
    }

    /**
     * 锁定指定库存数量，建议只提供给微服务远程调用
     */
    @PostMapping("lock_stock")
    public ResultObject<Object> lockStock(@Validated @RequestBody LockStockVo vo){
        wareInfoService.lockStock(vo);

        return ResultObject.ok();
    }

    /**
     * 解锁指定订单号库存数量，建议只提供给微服务远程调用
     */
    @PostMapping("unlock_stock")
    public ResultObject<Object> unlockStock(@RequestParam String orderRecord){
        wareInfoService.unlockStock(orderRecord);

        return ResultObject.ok();
    }

    /**
     * 解锁指定订单号库存并扣除库存，用于确认支付后扣除库存，建议只提供给微服务远程调用
     */
    @PostMapping("confirm_lock_stock_pay")
    public ResultObject<Object> confirmLockStockPay(@RequestParam String orderRecord){
        wareInfoService.confirmLockStockPay(orderRecord);

        return ResultObject.ok();
    }
}
