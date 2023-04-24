package neko.convenient.nekoconvenientproduct8005.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ESClientTest {
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Test
    public void upProduct() throws IOException {
        List<ProductInfoES> productInfoEss = new ArrayList<>();
        productInfoEss.add(new ProductInfoES().setSpuId("5")
                .setSkuId("5")
                .setSkuName("koori"));
        productInfoEss.add(new ProductInfoES().setSpuId("5")
                .setSkuId("6")
                .setSkuName("koori"));
        BulkRequest.Builder builder = new BulkRequest.Builder();
        for(ProductInfoES productInfoES : productInfoEss){
            builder.operations(operation->operation.index(idx->idx.index("neko_convenient")
                    .id(productInfoES.getSkuId())
                    .document(productInfoES)));
        }
        BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
        System.out.println(bulkResponse);
    }

    @Test
    public void downSingleProduct() throws IOException {
        DeleteResponse response = elasticsearchClient.delete(builder ->
                builder.index("neko_convenient")
                        .id("5"));
        System.out.println(response.shards().successful().intValue());
    }

    @Test
    public void downMultiplyProduct() throws IOException {
        DeleteByQueryResponse response = elasticsearchClient.deleteByQuery(builder ->
                builder.index("neko_convenient")
                        .query(q ->
                                q.term(t ->
                                        t.field("spuId")
                                                .value(5))));
        System.out.println(response.deleted());
    }
}
