package neko.convenient.nekoconvenientproduct8005.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("info")
    public ResultObject<MarketInfo> info(@RequestParam String marketId){
        return ResultObject.ok(marketInfoService.getMarketInfoByMarketId(marketId));
    }
}
