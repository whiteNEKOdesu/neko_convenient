package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import neko.convenient.nekoconvenientproduct8005.service.SkuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * sku信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("sku_info")
public class SkuInfoController {
    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 为指定spu新增sku
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PutMapping("new_sku_info")
    public ResultObject<Object> newSkuInfo(@Validated @RequestBody SkuInfoVo vo){
        skuInfoService.newSkuInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询指定spu中sku信息
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PostMapping("spu_sku_infos")
    public ResultObject<Page<SkuInfo>> spuSkuInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(skuInfoService.getSpuSkuInfoByQueryLimitedPage(vo));
    }

    /**
     * 获取指定skuId商店信息，建议只提供给微服务远程调用
     */
    @PostMapping("sku_id_market_info")
    public ResultObject<MarketInfo> skuIdMarketInfo(@RequestParam String skuId){
        return ResultObject.ok(skuInfoService.getMarketInfoBySkuId(skuId));
    }

    /**
     * 修改sku信息
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PostMapping("update_sku_info")
    public ResultObject<Object> updateSkuInfo(@Validated @RequestBody UpdateSkuInfoVo vo) throws IOException {
        skuInfoService.updateSkuInfoBySkuId(vo);

        return ResultObject.ok();
    }

    /**
     * 获取指定spuId商店信息，spu信息，sku信息
     */
    @PostMapping("market_product_info")
    public ResultObject<SpuAndSkuVo> marketProductInfo(@RequestParam String spuId) throws ExecutionException, InterruptedException {
        return ResultObject.ok(skuInfoService.getSkuInfosBySpuId(spuId));
    }

    /**
     * 根据skuId List获取sku信息，商店marketId，连锁店品牌基本信息，建议只提供给微服务远程调用
     */
    @PostMapping("product_infos")
    public ResultObject<List<ProductInfoVo>> productInfos(@RequestBody List<String> skuIds){
        return ResultObject.ok(skuInfoService.getProductInfosBySkuIds(skuIds));
    }

    /**
     * 根据orderRecord获取订单详情信息信息，建议只提供给微服务远程调用
     */
    @PostMapping("order_detail_infos")
    public ResultObject<List<OrderDetailInfoVo>> orderDetailInfos(@RequestParam String orderRecord){
        return ResultObject.ok(skuInfoService.getOrderDetailInfosByOrderRecord(orderRecord));
    }
}
