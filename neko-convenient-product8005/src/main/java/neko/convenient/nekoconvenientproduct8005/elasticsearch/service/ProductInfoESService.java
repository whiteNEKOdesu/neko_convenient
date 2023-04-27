package neko.convenient.nekoconvenientproduct8005.elasticsearch.service;

import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESQueryVo;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESVo;

import java.io.IOException;

public interface ProductInfoESService {
    ProductInfoESVo getProductInfoByQueryLimitedPage(ProductInfoESQueryVo vo) throws IOException;
}
