package neko.convenient.nekoconvenientorder8008.feign.product;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServiceName.PRODUCT_SERVICE, contextId = "SkuInfo")
public interface SkuInfoFeignService {
    @PostMapping("sku_info/product_infos")
    ResultObject<List<ProductInfoVo>> productInfos(@RequestBody List<String> skuIds);

    @PostMapping("sku_info/order_detail_infos")
    ResultObject<List<OrderDetailInfo>> orderDetailInfos(@RequestParam String orderRecord);
}
