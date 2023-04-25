package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;
import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import neko.convenient.nekoconvenientproduct8005.entity.MarketInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.CategoryInfoMapper;
import neko.convenient.nekoconvenientproduct8005.mapper.SpuInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.MarketInfoService;
import neko.convenient.nekoconvenientproduct8005.service.SkuInfoService;
import neko.convenient.nekoconvenientproduct8005.service.SpuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.SpuInfoVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * spu信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo> implements SpuInfoService {
    @Resource
    private MarketInfoService marketInfoService;

    @Resource
    private CategoryInfoMapper categoryInfoMapper;

    @Resource
    @Lazy
    private SkuInfoService skuInfoService;

    @Resource
    private ElasticsearchClient elasticsearchClient;

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

    /**
     * 上架商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upSpu(String spuId) throws IOException {
        SpuInfo spuInfo = this.baseMapper.selectById(spuId);
        if(spuInfo == null){
            throw new NoSuchResultException("spuId查询无结果");
        }

        //查询商店信息
        MarketInfo marketInfo = marketInfoService.getBaseMapper().selectOne(new QueryWrapper<MarketInfo>()
                .lambda()
                .eq(MarketInfo::getMarketId, spuInfo.getMarketId()));

        if(marketInfo == null || !StpUtil.getLoginId().toString().equals(marketInfo.getUid())){
            throw new NotPermissionException("权限不足");
        }

        //更新spu_info表上架状态
        this.baseMapper.updateSpuIsPublishBySpuId(spuId, true, LocalDateTime.now());

        //查询sku信息
        List<SkuInfo> skuInfos = skuInfoService.lambdaQuery().eq(SkuInfo::getSpuId, spuId).list();
        if(skuInfos.isEmpty()){
            throw new NoSuchResultException("spu中无sku，无法上架");
        }
        String marketAddressDescription = marketInfo.getAddress() + marketInfo.getMarketAddressDescription();
        //将sku信息收集为elasticsearch形式
        List<ProductInfoES> productInfoEss = skuInfos.stream().map(skuInfo -> {
            ProductInfoES productInfoES = new ProductInfoES();
            BeanUtil.copyProperties(skuInfo, productInfoES);
            return productInfoES.setSpuName(spuInfo.getSpuName())
                    .setSpuDescription(spuInfo.getSpuDescription())
                    .setSpuImage(spuInfo.getSpuImage())
                    .setCategoryId(spuInfo.getCategoryId())
                    .setCategoryName(spuInfo.getCategoryName())
                    .setBrandId(marketInfo.getBrandId())
                    .setBrandName(marketInfo.getBrandName())
                    .setBrandLogo(marketInfo.getBrandLogo())
                    .setMarketAddressDescription(marketAddressDescription)
                    .setAddressId(marketInfo.getAddressId())
                    .setAddress(marketInfo.getAddress());
        }).collect(Collectors.toList());

        //向elasticsearch添加商品信息
        BulkRequest.Builder builder = new BulkRequest.Builder();
        for(ProductInfoES productInfoES : productInfoEss){
            builder.operations(operation->operation.index(idx->idx.index("neko_convenient")
                    .id(productInfoES.getSkuId())
                    .document(productInfoES)));
        }
        BulkResponse bulkResponse;
        try {
            bulkResponse = elasticsearchClient.bulk(builder.build());
        }catch (Exception e){
            //上架失败，删除elasticsearch商品信息
            innerDownSpu(spuId);
            throw e;
        }

        log.info("商品上架spuId: " + spuId + "，" + bulkResponse.toString());
    }

    /**
     * 下架商品
     */
    @Override
    public void downSpu(String spuId) throws IOException {
        SpuInfo spuInfo = this.baseMapper.selectById(spuId);
        if(spuInfo == null){
            throw new NoSuchResultException("spuId查询无结果");
        }

        //查询商店信息
        MarketInfo marketInfo = marketInfoService.getBaseMapper().selectOne(new QueryWrapper<MarketInfo>()
                .lambda()
                .eq(MarketInfo::getMarketId, spuInfo.getMarketId()));

        if(marketInfo == null || !StpUtil.getLoginId().toString().equals(marketInfo.getUid())){
            throw new NotPermissionException("权限不足");
        }

        innerDownSpu(spuId);
    }

    /**
     * 下架商品，本类内部调用
     */
    private void innerDownSpu(String spuId) throws IOException {
        //更新spu_info表上架状态
        this.baseMapper.updateSpuIsPublishBySpuId(spuId, false, LocalDateTime.now());
        DeleteByQueryResponse response = elasticsearchClient.deleteByQuery(builder ->
                builder.index(Constant.ELASTIC_SEARCH_INDEX)
                        .query(q ->
                                q.term(t ->
                                        t.field("spuId")
                                                .value(spuId))));

        log.info("下架商品spuId: " + spuId + "，下架sku数量: " + response.deleted());
    }
}
