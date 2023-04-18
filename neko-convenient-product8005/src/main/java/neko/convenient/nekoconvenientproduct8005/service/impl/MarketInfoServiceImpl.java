package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.MarketInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
     * 分页查询用户自身商店信息
     */
    @Override
    public Page<MarketInfo> getUserSelfMarketInfoByQueryLimitedPage(QueryVo vo) {
        Page<MarketInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<MarketInfo> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(MarketInfo::getBrandName, vo.getQueryWords());
        }
        queryWrapper.lambda().eq(MarketInfo::getUid, StpUtil.getLoginId().toString())
                .eq(MarketInfo::getIsDelete, 0);

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
