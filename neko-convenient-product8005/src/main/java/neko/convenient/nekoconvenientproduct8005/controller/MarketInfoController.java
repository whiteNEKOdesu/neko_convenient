package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 商店信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("market_info")
public class MarketInfoController {
    @Resource
    private MarketInfoService marketInfoService;

    /**
     * 根据商店id获取商店信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @GetMapping("info")
    public ResultObject<MarketInfo> info(@RequestParam String marketId){
        return ResultObject.ok(marketInfoService.getMarketInfoByMarketId(marketId));
    }

    /**
     * 分页查询用户自身商店信息
     */
    @SaCheckRole(RoleType.MARKET)
    @SaCheckLogin
    @PostMapping("user_market_infos")
    public ResultObject<Page<MarketInfo>> userMarketInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(marketInfoService.getUserSelfMarketInfoByQueryLimitedPage(vo));
    }
}
