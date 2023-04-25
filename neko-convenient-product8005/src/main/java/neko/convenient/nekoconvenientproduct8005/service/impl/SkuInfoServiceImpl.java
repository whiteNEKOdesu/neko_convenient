package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.SkuInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import neko.convenient.nekoconvenientproduct8005.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientproduct8005.service.SpuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.SkuInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.UpdateSkuInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
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
@Slf4j
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Resource
    private SpuInfoService spuInfoService;

    @Resource
    private MarketInfoService marketInfoService;

    @Resource
    private ElasticsearchClient elasticsearchClient;

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
        skuInfo.setMarketId(spuInfo.getMarketId())
                .setCreateTime(now)
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

    /**
     * 获取指定skuId商店信息
     */
    @Override
    public MarketInfo getMarketInfoBySkuId(String skuId) {
        SkuInfo skuInfo = this.baseMapper.selectById(skuId);
        if(skuInfo == null){
            throw new NoSuchResultException("skuId查询无结果");
        }

        return marketInfoService.getBaseMapper().selectById(skuInfo.getMarketId());
    }

    /**
     * 更新sku信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSkuInfoBySkuId(UpdateSkuInfoVo vo) throws IOException {
        if(vo.getSkuName() == null && vo.getSkuImage() == null && vo.getPrice() == null){
            throw new IllegalArgumentException("SkuInfo更新参数不能全为null");
        }

        SkuInfo skuInfo = this.baseMapper.selectById(vo.getSkuId());
        if(skuInfo == null){
            throw new NoSuchResultException("spuId查询无结果");
        }

        //查询商店信息
        MarketInfo marketInfo = marketInfoService.getBaseMapper().selectOne(new QueryWrapper<MarketInfo>()
                .lambda()
                .eq(MarketInfo::getMarketId, skuInfo.getMarketId()));

        if(marketInfo == null || !StpUtil.getLoginId().toString().equals(marketInfo.getUid())){
            throw new NotPermissionException("权限不足");
        }

        SkuInfo todoUpdate = new SkuInfo();
        BeanUtil.copyProperties(vo, todoUpdate);

        //更新sku信息
        this.baseMapper.updateById(todoUpdate);

        //修改elasticsearch中sku数据
        ProductInfoES productInfoES = new ProductInfoES();
        productInfoES.setSkuName(vo.getSkuName())
                .setSkuImage(vo.getSkuImage())
                .setPrice(vo.getPrice());
        UpdateResponse<ProductInfoES> response = elasticsearchClient.update(builder ->
                builder.index(Constant.ELASTIC_SEARCH_INDEX)
                        .id(vo.getSkuId())
                        .doc(productInfoES)
                        //表示修改而不是覆盖
                        .docAsUpsert(true), ProductInfoES.class);
        log.info("修改elasticsearch中sku数据，skuId: " + vo.getSkuId() + "，修改数量: " + response.shards().successful().intValue());
    }
}
