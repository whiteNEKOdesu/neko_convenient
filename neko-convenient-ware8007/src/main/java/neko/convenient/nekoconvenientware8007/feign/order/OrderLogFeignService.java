package neko.convenient.nekoconvenientware8007.feign.order;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientware8007.to.OrderLogTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.ORDER_SERVICE)
public interface OrderLogFeignService {
    ResultObject<OrderLogTo> preorderStatus(@RequestParam String orderRecord);
}
