package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.CategoryInfoMapper;
import neko.convenient.nekoconvenientproduct8005.mapper.SpuInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import neko.convenient.nekoconvenientproduct8005.service.SpuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.SpuInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * spu信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo> implements SpuInfoService {
    @Resource
    private MarketInfoService marketInfoService;

    @Resource
    private CategoryInfoMapper categoryInfoMapper;

    /**
     * 新增商品
     */
    @Override
    public void newSpuInfo(SpuInfoVo vo) {
        MarketInfo marketInfo = marketInfoService.getMarketInfoByMarketId(vo.getMarketId());
        if(marketInfo == null || !StpUtil.getLoginId().toString().equals(marketInfo.getUid())){
            throw new NotPermissionException("权限不足");
        }

        String categoryName = categoryInfoMapper.getFullCategoryName(vo.getCategoryId());
        if(categoryName == null){
            throw new NotPermissionException("权限不足");
        }

        SpuInfo spuInfo = new SpuInfo();
        BeanUtil.copyProperties(vo, spuInfo);
        LocalDateTime now = LocalDateTime.now();
        spuInfo.setCategoryName(categoryName)
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(spuInfo);
    }

    /**
     * 分页查询指定商店商品信息
     */
    @Override
    public Page<SpuInfo> getMarketSpuInfoByQueryLimitedPage(QueryVo vo) {
        if(vo.getObjectId() == null){
            throw new IllegalArgumentException("缺少marketId");
        }
        Page<SpuInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SpuInfo::getMarketId, vo.getObjectId());
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(SpuInfo::getSpuName, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
