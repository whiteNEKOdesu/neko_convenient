package neko.convenient.nekoconvenientproduct8005.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.MarketInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商店信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class MarketInfoServiceImpl extends ServiceImpl<MarketInfoMapper, MarketInfo> implements MarketInfoService {

    /**
     * 根据商店id获取商店信息
     */
    @Override
    public MarketInfo getMarketInfoByMarketId(String marketId) {
        return this.baseMapper.selectById(marketId);
    }

    /**
     * 根据uid获取商店信息
     */
    @Override
    public MarketInfo getMarketInfoByUid(String uid) {
        return this.baseMapper.selectOne(new QueryWrapper<MarketInfo>().lambda()
            .eq(MarketInfo::getUid, uid)
            .eq(MarketInfo::getIsDelete, 0));
    }
}
