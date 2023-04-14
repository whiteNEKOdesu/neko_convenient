package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ApplyStatusIllegalException;
import neko.convenient.nekoconvenientproduct8005.entity.ApplyInfo;
import neko.convenient.nekoconvenientproduct8005.entity.BrandInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.ApplyInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.ApplyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientproduct8005.service.BrandInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.AdminApplyInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.ApplyInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 品牌申请表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class ApplyInfoServiceImpl extends ServiceImpl<ApplyInfoMapper, ApplyInfo> implements ApplyInfoService {
    @Resource
    private BrandInfoService brandInfoService;

    /**
     * 申请连锁店品牌
     */
    @Override
    public void applyBrand(ApplyInfoVo vo) {
        ApplyInfo applyInfo = new ApplyInfo();
        BeanUtil.copyProperties(vo, applyInfo);
        LocalDateTime now = LocalDateTime.now();
        applyInfo.setUid(StpUtil.getLoginId().toString())
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(applyInfo);
    }

    /**
     * 分页查询用户自身申请信息
     */
    @Override
    public Page<ApplyInfo> getUserSelfApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<ApplyInfo> page = new Page<>(vo.pageOrLimitWhenOverFlow(), vo.getLimited());
        QueryWrapper<ApplyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApplyInfo::getUid, StpUtil.getLoginId().toString());
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(ApplyInfo::getBrandName, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 分页查询所有用户申请信息
     */
    @Override
    public Page<ApplyInfo> getApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<ApplyInfo> page = new Page<>(vo.pageOrLimitWhenOverFlow(), vo.getLimited());
        QueryWrapper<ApplyInfo> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(ApplyInfo::getBrandName, vo.getQueryWords());
        }
        queryWrapper.lambda().eq(ApplyInfo::getStatus, Byte.valueOf("-1"));

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 管理员处理申请信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleApply(AdminApplyInfoVo vo) {
        ApplyInfo applyInfo = this.baseMapper.selectById(vo.getApplyId());
        if(applyInfo == null || !applyInfo.getStatus().equals(Byte.valueOf("-1"))){
            throw new ApplyStatusIllegalException("申请状态非法");
        }

        //通过申请
        if(vo.getStatus().equals(Byte.valueOf("1"))){
            BrandInfo brandInfo = new BrandInfo();
            LocalDateTime now = LocalDateTime.now();
            brandInfo.setUid(applyInfo.getUid())
                    .setBrandName(applyInfo.getBrandName())
                    .setDescription(applyInfo.getBrandDescription())
                    .setCertificateUrl(applyInfo.getCertificateUrl())
                    .setCreateTime(now)
                    .setUpdateTime(now);
            brandInfoService.save(brandInfo);
        }

        //修改申请状态
        this.baseMapper.updateStatusByApplyId(vo.getApplyId(),
                StpUtil.getLoginId().toString(),
                vo.getStatus(),
                vo.getStatusInfo(),
                LocalDateTime.now());
    }
}
