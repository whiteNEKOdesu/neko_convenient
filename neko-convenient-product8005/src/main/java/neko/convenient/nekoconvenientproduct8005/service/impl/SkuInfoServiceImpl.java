package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.SkuInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import neko.convenient.nekoconvenientproduct8005.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientproduct8005.service.SpuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.SkuInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * sku信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Resource
    private SpuInfoService spuInfoService;

    @Resource
    private MarketInfoService marketInfoService;

    /**
     * 为指定spu新增sku
     */
    @Override
    public void newSkuInfo(SkuInfoVo vo) {
        SpuInfo spuInfo = spuInfoService.getById(vo.getSpuId());
        if(spuInfo == null || !StpUtil.getLoginId().toString().equals(marketInfoService.getMarketInfoByMarketId(spuInfo.getMarketId()).getUid())){
            throw new NotPermissionException("权限不足");
        }

        SkuInfo skuInfo = new SkuInfo();
        BeanUtil.copyProperties(vo, skuInfo);
        LocalDateTime now = LocalDateTime.now();
        skuInfo.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(skuInfo);
    }

    /**
     * 分页查询指定spu中sku信息
     */
    @Override
    public Page<SkuInfo> getSpuSkuInfoByQueryLimitedPage(QueryVo vo) {
        if(vo.getObjectId() == null){
            throw new IllegalArgumentException("缺少spuId");
        }
        Page<SkuInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<SkuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SkuInfo::getSpuId, vo.getObjectId());
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(SkuInfo::getSkuName, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
