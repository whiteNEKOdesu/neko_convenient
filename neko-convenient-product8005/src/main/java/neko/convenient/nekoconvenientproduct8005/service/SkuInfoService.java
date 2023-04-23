package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientproduct8005.vo.SkuInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.UpdateSkuInfoVo;

/**
 * <p>
 * sku信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface SkuInfoService extends IService<SkuInfo> {
    void newSkuInfo(SkuInfoVo vo);

    Page<SkuInfo> getSpuSkuInfoByQueryLimitedPage(QueryVo vo);

    MarketInfo getMarketInfoBySkuId(String skuId);

    void updateSkuInfoBySkuId(UpdateSkuInfoVo vo);
}
