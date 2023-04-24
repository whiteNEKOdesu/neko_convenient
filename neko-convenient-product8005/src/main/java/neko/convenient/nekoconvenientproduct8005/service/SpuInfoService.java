package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientproduct8005.vo.SpuInfoVo;

import java.io.IOException;

/**
 * <p>
 * spu信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface SpuInfoService extends IService<SpuInfo> {
    void newSpuInfo(SpuInfoVo vo);

    Page<SpuInfo> getMarketSpuInfoByQueryLimitedPage(QueryVo vo);

    void upSpu(String spuId) throws IOException;

    void downSpu(String spuId) throws IOException;
}
