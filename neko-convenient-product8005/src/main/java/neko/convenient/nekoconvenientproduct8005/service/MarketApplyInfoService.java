package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketApplyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientproduct8005.vo.AdminMarketApplyInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.MarketApplyInfoVo;

/**
 * <p>
 * 商店开店申请表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface MarketApplyInfoService extends IService<MarketApplyInfo> {
    void applyMarket(MarketApplyInfoVo vo);

    Page<MarketApplyInfo> getUserSelfMarketApplyInfoByQueryLimitedPage(QueryVo vo);

    Page<MarketApplyInfo> getMarketApplyInfoByQueryLimitedPage(QueryVo vo);

    void handleApply(AdminMarketApplyInfoVo vo);
}
