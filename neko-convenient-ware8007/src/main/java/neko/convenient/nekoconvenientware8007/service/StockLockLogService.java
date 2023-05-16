package neko.convenient.nekoconvenientware8007.service;

import neko.convenient.nekoconvenientware8007.entity.StockLockLog;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientware8007.vo.LockProductInfoVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 库存锁定日志表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
public interface StockLockLogService extends IService<StockLockLog> {
    void newStockLockLog(StockLockLog stockLockLog);

    void updateStockLockLogStatus(String stockLockLogId,
                                  Byte status,
                                  LocalDateTime updateTime);

    List<LockProductInfoVo> getSkuIdsByOrderRecord(String orderRecord);

    List<StockLockLog> getLockStockLockLogByOrderRecord(String orderRecord);
}
