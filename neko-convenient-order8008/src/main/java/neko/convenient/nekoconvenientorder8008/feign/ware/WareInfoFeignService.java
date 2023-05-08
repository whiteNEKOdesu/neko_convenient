package neko.convenient.nekoconvenientorder8008.feign.ware;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientorder8008.to.LockStockTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.WARE_SERVICE)
public interface WareInfoFeignService {
    @PostMapping("ware_info/lock_stock")
    ResultObject<Object> lockStock(@RequestBody LockStockTo to);

    @PostMapping("ware_info/unlock_stock")
    ResultObject<Object> unlockStock(@RequestParam String orderRecord);
}
