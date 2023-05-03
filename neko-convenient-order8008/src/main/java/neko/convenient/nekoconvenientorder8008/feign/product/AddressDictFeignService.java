package neko.convenient.nekoconvenientorder8008.feign.product;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.PRODUCT_SERVICE)
public interface AddressDictFeignService {
    @GetMapping("address_dict/address_info")
    ResultObject<String> addressInfo(@RequestParam Integer addressId);
}
