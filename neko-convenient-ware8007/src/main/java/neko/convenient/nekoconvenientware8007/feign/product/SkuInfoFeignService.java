package neko.convenient.nekoconvenientware8007.feign.product;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientware8007.to.MarketInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.PRODUCT_SERVICE)
public interface SkuInfoFeignService {
    @PostMapping("sku_info/sku_id_market_info")
    ResultObject<MarketInfoTo> skuIdMarketInfo(@RequestParam String skuId);
}
