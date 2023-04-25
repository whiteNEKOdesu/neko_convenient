package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import neko.convenient.nekoconvenientproduct8005.service.SpuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.SpuInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 * spu信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("spu_info")
public class SpuInfoController {
    @Resource
    private SpuInfoService spuInfoService;

    /**
     * 新增商品
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PutMapping("new_spu_info")
    public ResultObject<Object> newSpuInfo(@Validated @RequestBody SpuInfoVo vo){
        spuInfoService.newSpuInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询指定商店商品信息
     */
    @PostMapping("market_spu_infos")
    public ResultObject<Page<SpuInfo>> marketSpuInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(spuInfoService.getMarketSpuInfoByQueryLimitedPage(vo));
    }

    /**
     * 上架商品
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PutMapping("up_spu")
    public ResultObject<Object> upSpu(@RequestParam String spuId) throws IOException {
        spuInfoService.upSpu(spuId);

        return ResultObject.ok();
    }

    /**
     * 下架商品
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PostMapping("down_spu")
    public ResultObject<Object> downSpu(@RequestParam String spuId) throws IOException {
        spuInfoService.downSpu(spuId);

        return ResultObject.ok();
    }
}
