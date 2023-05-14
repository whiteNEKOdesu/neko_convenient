package neko.convenient.nekoconvenientproduct8005.feign.ware;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientproduct8005.to.LockProductInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServiceName.WARE_SERVICE, contextId = "StockLockLog")
public interface StockLockLogFeignService {
    @PostMapping("stock_lock_log/order_record_sku_id_infos")
    ResultObject<List<LockProductInfoTo>> orderRecordSkuIdInfos(@RequestParam String orderRecord);
}
