package neko.convenient.nekoconvenientorder8008.feign.product;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = ServiceName.PRODUCT_SERVICE, contextId = "PointDict")
public interface PointDictFeignService {
    @PostMapping("point_dict/price_point")
    ResultObject<Integer> pricePoint(@RequestParam BigDecimal price);
}
