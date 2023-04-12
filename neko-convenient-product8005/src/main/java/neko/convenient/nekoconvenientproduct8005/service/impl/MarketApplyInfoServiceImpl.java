package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketApplyInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.MarketApplyInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketApplyInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.MarketApplyInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 商店开店申请表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class MarketApplyInfoServiceImpl extends ServiceImpl<MarketApplyInfoMapper, MarketApplyInfo> implements MarketApplyInfoService {

    /**
     * 申请开店
     */
    @Override
    public void applyMarket(MarketApplyInfoVo vo) {
        MarketApplyInfo marketApplyInfo = new MarketApplyInfo();
        BeanUtil.copyProperties(vo, marketApplyInfo);
        LocalDateTime now = LocalDateTime.now();
        marketApplyInfo.setUid(StpUtil.getLoginId().toString())
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(marketApplyInfo);
    }

    /**
     * 分页查询申请信息
     */
    @Override
    public Page<MarketApplyInfo> getMarketApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<MarketApplyInfo> page = new Page<>(vo.pageOrLimitWhenOverFlow(), vo.getLimited());
        QueryWrapper<MarketApplyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketApplyInfo::getUid, StpUtil.getLoginId().toString());
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
