package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ApplyStatusIllegalException;
import neko.convenient.nekoconvenientproduct8005.entity.BrandInfo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketApplyInfo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.MarketApplyInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.BrandInfoService;
import neko.convenient.nekoconvenientproduct8005.service.MarketApplyInfoService;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.AdminMarketApplyInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.MarketApplyInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Resource
    private MarketInfoService marketInfoService;

    @Resource
    private BrandInfoService brandInfoService;

    /**
     * 申请开店
     */
    @Override
    public void applyMarket(MarketApplyInfoVo vo) {
        MarketApplyInfo marketApplyInfo = new MarketApplyInfo();
        BeanUtil.copyProperties(vo, marketApplyInfo);
        LocalDateTime now = LocalDateTime.now();
        marketApplyInfo.setUid(StpUtil.getLoginId().toString())
                .setBrandId(vo.getBrandId())
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(marketApplyInfo);
    }

    /**
     * 分页查询用户自身申请信息
     */
    @Override
    public Page<MarketApplyInfo> getUserSelfMarketApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<MarketApplyInfo> page = new Page<>(vo.pageOrLimitWhenOverFlow(), vo.getLimited());
        QueryWrapper<MarketApplyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketApplyInfo::getUid, StpUtil.getLoginId().toString());
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 分页查询所有用户申请信息
     */
    @Override
    public Page<MarketApplyInfo> getMarketApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<MarketApplyInfo> page = new Page<>(vo.pageOrLimitWhenOverFlow(), vo.getLimited());
        QueryWrapper<MarketApplyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketApplyInfo::getStatus, Byte.valueOf("-1"));
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 管理员处理申请信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleApply(AdminMarketApplyInfoVo vo) {
        MarketApplyInfo marketApplyInfo = this.baseMapper.selectById(vo.getApplyId());
        if(marketApplyInfo == null || !marketApplyInfo.getStatus().equals(Byte.valueOf("-1"))){
            throw new ApplyStatusIllegalException("申请状态非法");
        }

        //通过申请
        if(vo.getStatus().equals(Byte.valueOf("1"))){
            BrandInfo brandInfo = brandInfoService.getById(marketApplyInfo.getBrandId());
            MarketInfo marketInfo = new MarketInfo();
            LocalDateTime now = LocalDateTime.now();
            marketInfo.setUid(marketApplyInfo.getUid())
                    .setBrandId(marketApplyInfo.getBrandId())
                    .setBrandName(brandInfo.getBrandName())
                    .setBrandLogo(brandInfo.getLogoUrl())
                    .setMarketAddressDescription(marketApplyInfo.getMarketAddressDescription())
                    .setCertificateUrl(marketApplyInfo.getCertificateUrl())
                    .setAddressId(marketApplyInfo.getAddressId())
                    .setCreateTime(now)
                    .setUpdateTime(now);
            marketInfoService.save(marketInfo);
        }

        //修改申请状态
        this.baseMapper.updateStatusByApplyId(vo.getApplyId(),
                StpUtil.getLoginId().toString(),
                vo.getStatus(),
                vo.getStatusInfo(),
                LocalDateTime.now());
    }
}
