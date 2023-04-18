package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商店信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface MarketInfoService extends IService<MarketInfo> {
    MarketInfo getMarketInfoByMarketId(String marketId);

    Page<MarketInfo> getUserSelfMarketInfoByQueryLimitedPage(QueryVo vo);
}
