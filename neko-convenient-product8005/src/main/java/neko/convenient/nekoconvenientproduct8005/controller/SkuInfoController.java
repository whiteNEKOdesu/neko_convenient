package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import neko.convenient.nekoconvenientproduct8005.service.SkuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.SkuInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @PostMapping("spu_sku_infos")
    public ResultObject<Page<SkuInfo>> spuSkuInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(skuInfoService.getSpuSkuInfoByQueryLimitedPage(vo));
    }
}
