package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleIds;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ApplyStatusIllegalException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.MemberServiceException;
import neko.convenient.nekoconvenientproduct8005.entity.BrandInfo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketApplyInfo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.feign.member.UserRoleRelationFeignService;
import neko.convenient.nekoconvenientproduct8005.mapper.AddressDictMapper;
import neko.convenient.nekoconvenientproduct8005.mapper.MarketApplyInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.BrandInfoService;
import neko.convenient.nekoconvenientproduct8005.service.MarketApplyInfoService;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import neko.convenient.nekoconvenientproduct8005.to.NewUserRoleRelationTo;
import neko.convenient.nekoconvenientproduct8005.vo.AdminMarketApplyInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.MarketApplyInfoVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;

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

    @Resource
    private UserRoleRelationFeignService userRoleRelationFeignService;

    @Resource
    private AddressDictMapper addressDictMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 申请开店
     */
    @Override
    public void applyMarket(MarketApplyInfoVo vo) {
        MarketApplyInfo marketApplyInfo = new MarketApplyInfo();
        BeanUtil.copyProperties(vo, marketApplyInfo);
        LocalDateTime now = LocalDateTime.now();
        BrandInfo brandInfo = brandInfoService.getById(vo.getBrandId());
        marketApplyInfo.setUid(StpUtil.getLoginId().toString())
                .setBrandId(vo.getBrandId())
                .setBrandName(brandInfo.getBrandName())
                .setAddress(addressDictMapper.getAddressInfoByAddressId(vo.getAddressId()))
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(marketApplyInfo);
    }

    /**
     * 分页查询用户自身申请信息
     */
    @Override
    public Page<MarketApplyInfo> getUserSelfMarketApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<MarketApplyInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
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
        Page<MarketApplyInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
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
                    .setAddress(marketApplyInfo.getAddress())
                    .setCreateTime(now)
                    .setUpdateTime(now);
            marketInfoService.save(marketInfo);

            NewUserRoleRelationTo to = new NewUserRoleRelationTo();
            to.setUid(marketApplyInfo.getUid())
                    .setRoleIds(Collections.singletonList(RoleIds.MARKET));
            //为用户添加market角色
            ResultObject<Object> r = userRoleRelationFeignService.newUserRoleRelation(to);
            if(!r.getResponseCode().equals(200)){
                throw new MemberServiceException("member微服务添加角色错误");
            }
            String key = Constant.MEMBER_REDIS_PREFIX + "weight_cache:" + marketApplyInfo.getUid();
            //删除缓存
            stringRedisTemplate.delete(key);
        }

        //修改申请状态
        this.baseMapper.updateStatusByApplyId(vo.getApplyId(),
                StpUtil.getLoginId().toString(),
                vo.getStatus(),
                vo.getStatusInfo(),
                LocalDateTime.now());
    }
}
