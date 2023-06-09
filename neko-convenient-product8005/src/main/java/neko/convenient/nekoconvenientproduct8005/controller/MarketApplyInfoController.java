package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.MarketApplyInfo;
import neko.convenient.nekoconvenientproduct8005.service.MarketApplyInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.AdminMarketApplyInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.MarketApplyInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 商店开店申请表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("market_apply_info")
public class MarketApplyInfoController {
    @Resource
    private MarketApplyInfoService marketApplyInfoService;

    /**
     * 申请开店
     */
    @SaCheckLogin
    @PutMapping("apply_market")
    public ResultObject<Object> applyMarket(@Validated @RequestBody MarketApplyInfoVo vo){
        marketApplyInfoService.applyMarket(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询申请信息
     */
    @SaCheckLogin
    @PostMapping("apply_status_info")
    public ResultObject<Page<MarketApplyInfo>> applyStatusInfo(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(marketApplyInfoService.getUserSelfMarketApplyInfoByQueryLimitedPage(vo));
    }

    /**
     * 分页查询所有用户申请信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("market_apply_infos")
    public ResultObject<Page<MarketApplyInfo>> applyInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(marketApplyInfoService.getMarketApplyInfoByQueryLimitedPage(vo));
    }

    /**
     * 管理员处理申请信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("handle_apply")
    public ResultObject<Object> handleApply(@Validated @RequestBody AdminMarketApplyInfoVo vo){
        marketApplyInfoService.handleApply(vo);

        return ResultObject.ok();
    }
}
