package neko.convenient.nekoconvenientproduct8005.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.service.ProductInfoESService;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESQueryVo;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductInfoESServiceImpl implements ProductInfoESService {
    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * 查询商品
     */
    @Override
    public ProductInfoESVo getProductInfoByQueryLimitedPage(ProductInfoESQueryVo vo) throws IOException {
        //构建查询请求
        SearchRequest request = buildSearchRequest(vo);
        log.info("elasticsearch语句: " + request.toString());
        SearchResponse<ProductInfoES> response = elasticsearchClient.search(buildSearchRequest(vo), ProductInfoES.class);

        //拆解查询响应结果为vo
        ProductInfoESVo searchVo = getSearchVo(response);
        return searchVo.setSize(vo.getLimited())
                .setCurrent(vo.getCurrentPage());
    }

    /**
     * 构建查询请求
     */
    private SearchRequest buildSearchRequest(ProductInfoESQueryVo vo){
        SearchRequest.Builder builder = new SearchRequest.Builder();
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if(StringUtils.hasText(vo.getBrandId())){
            //按照brandId筛选
            boolBuilder.filter(f ->
                    f.term(t ->
                            t.field("brandId")
                                    .value(vo.getBrandId())));
        }

        if(StringUtils.hasText(vo.getMarketId())){
            //按照marketId筛选
            boolBuilder.filter(f ->
                    f.term(t ->
                            t.field("marketId")
                                    .value(vo.getMarketId())));
        }

        if(vo.getCategoryId() != null){
            //按照分类id筛选
            boolBuilder.filter(f ->
                    f.term(t ->
                            t.field("categoryId")
                                    .value(vo.getCategoryId())));
        }

        if(vo.getAddressId() != null){
            //按照地址id筛选
            boolBuilder.filter(f ->
                    f.term(t ->
                            t.field("addressId")
                                    .value(vo.getAddressId())));
        }

        if(vo.getMinPrice() != null && vo.getMaxPrice() != null && vo.getMinPrice().compareTo(vo.getMaxPrice()) < 0){
            //按照价格范围筛选
            boolBuilder.filter(f ->
                    f.range(r ->
                            r.field("price")
                                    .gte(JsonData.of(vo.getMinPrice()))
                                    .lte(JsonData.of(vo.getMaxPrice()))));
        }

        if(StringUtils.hasText(vo.getQueryWords())){
            boolBuilder.must(m ->
                    m.match(mt ->
                            mt.field("spuDescription")
                                    .query(vo.getQueryWords())))
                    .should(s -> s.match(m -> m.field("spuName").query(vo.getQueryWords())))
                    .should(s -> s.match(m -> m.field("skuName").query(vo.getQueryWords())))
                    .should(s -> s.match(m -> m.field("categoryName").query(vo.getQueryWords())))
                    .should(s -> s.match(m -> m.field("brandName").query(vo.getQueryWords())))
                    .should(s -> s.match(m -> m.field("marketAddressDescription").query(vo.getQueryWords())));
        }

        return builder.index(Constant.ELASTIC_SEARCH_INDEX)
                .query(q ->
                        q.bool(boolBuilder.build()))
                .from(vo.getFrom())
                .size(vo.getLimited())
                .highlight(h ->
                        h.fields("skuTitle", hf -> hf)
                                .preTags("<b style='color:red'>")
                                .postTags("</b>"))
                .build();
    }

    /**
     * 拆解查询响应结果为vo
     */
    private ProductInfoESVo getSearchVo(SearchResponse<ProductInfoES> response){
        ProductInfoESVo productInfoESVo = new ProductInfoESVo();
        List<ProductInfoES> result = new ArrayList<>();

        List<Hit<ProductInfoES>> hits = response.hits().hits();
        if(hits != null && !hits.isEmpty()){
            for(Hit<ProductInfoES> hit : hits){
                result.add(hit.source());
            }
        }

        return productInfoESVo.setRecords(result)
                .setTotal(response.hits().total() != null ? Integer.parseInt(response.hits().total().value() + "") : 0);
    }
}
