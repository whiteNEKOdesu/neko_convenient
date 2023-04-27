package neko.convenient.nekoconvenientproduct8005.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.service.ProductInfoESService;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESQueryVo;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoESVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("elastic_search")
public class ElasticSearchController {
    @Resource
    private ProductInfoESService productInfoESService;

    /**
     * 分页查询所有商品
     */
    @PostMapping("product_infos")
    public ResultObject<ProductInfoESVo> productInfos(@Validated @RequestBody ProductInfoESQueryVo vo) throws IOException {
        return ResultObject.ok(productInfoESService.getProductInfoByQueryLimitedPage(vo));
    }
}
