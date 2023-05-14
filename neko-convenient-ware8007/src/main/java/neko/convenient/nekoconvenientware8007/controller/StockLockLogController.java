package neko.convenient.nekoconvenientware8007.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientware8007.service.StockLockLogService;
import neko.convenient.nekoconvenientware8007.vo.LockProductInfoVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 库存锁定日志表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@RestController
@RequestMapping("stock_lock_log")
public class StockLockLogController {
    @Resource
    private StockLockLogService stockLockLogService;

    /**
     * 根据orderRecord查询锁定商品信息，建议只提供给微服务远程调用
     */
    @PostMapping("order_record_sku_id_infos")
    public ResultObject<List<LockProductInfoVo>> orderRecordSkuIdInfos(@RequestParam String orderRecord){
        return ResultObject.ok(stockLockLogService.getSkuIdsByOrderRecord(orderRecord));
    }
}
