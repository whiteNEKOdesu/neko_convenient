package neko.convenient.nekoconvenientproduct8005.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.service.ProductInfoESService;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductInfoESServiceImpl implements ProductInfoESService {
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Override
    public List<ProductInfoES> getProductInfoByQueryLimitedPage(ProductInfoESQueryVo vo) {
        Query.Builder builder = new Query.Builder();
        if(StringUtils.hasText(vo.getBrandId())){
            builder.bool(b ->
                    b.filter(f ->
                            f.term(t ->
                                    t.field("brandId")
                                    .value(vo.getBrandId()))));
        }

        if(StringUtils.hasText(vo.getMarketId())){
            builder.bool(b ->
                    b.filter(f ->
                            f.term(t ->
                                    t.field("marketId")
                                            .value(vo.getMarketId()))));
        }

        if(vo.getCategoryId() != null){
            builder.bool(b ->
                    b.filter(f ->
                            f.term(t ->
                                    t.field("categoryId")
                                            .value(vo.getCategoryId()))));
        }

        if(vo.getAddressId() != null){
            builder.bool(b ->
                    b.filter(f ->
                            f.term(t ->
                                    t.field("addressId")
                                            .value(vo.getAddressId()))));
        }

        if(vo.getMinPrice() != null && vo.getMaxPrice() != null && vo.getMinPrice().compareTo(vo.getMaxPrice()) < 0){
            builder.bool(b ->
                    b.filter(f ->
                            f.range(r ->
                                    r.field("price")
                                            .gte(JsonData.of(vo.getMinPrice()))
                                            .lte(JsonData.of(vo.getMaxPrice())))));
        }

        builder.bool(b ->
                b.must(m ->
                        m.match(mt ->
                                mt.field("spuDescription")
                                        .query(vo.getQueryWords())))
                        .should(s -> s.match(m -> m.field("spuName").query(vo.getQueryWords())))
                        .should(s -> s.match(m -> m.field("skuName").query(vo.getQueryWords())))
                        .should(s -> s.match(m -> m.field("categoryName").query(vo.getQueryWords())))
                        .should(s -> s.match(m -> m.field("brandName").query(vo.getQueryWords())))
                        .should(s -> s.match(m -> m.field("marketAddressDescription").query(vo.getQueryWords()))));
        return null;
    }
}
