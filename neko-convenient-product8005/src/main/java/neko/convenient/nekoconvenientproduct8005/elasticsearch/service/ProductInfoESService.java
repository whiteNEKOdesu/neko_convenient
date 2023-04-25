package neko.convenient.nekoconvenientproduct8005.elasticsearch.service;

import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESQueryVo;

import java.util.List;

public interface ProductInfoESService {
    List<ProductInfoES> getProductInfoByQueryLimitedPage(ProductInfoESQueryVo vo);
}
