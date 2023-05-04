package neko.convenient.nekoconvenientware8007.service.impl;

import neko.convenient.nekoconvenientware8007.entity.StockLockLog;
import neko.convenient.nekoconvenientware8007.mapper.StockLockLogMapper;
import neko.convenient.nekoconvenientware8007.service.StockLockLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 库存锁定日志表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Service
public class StockLockLogServiceImpl extends ServiceImpl<StockLockLogMapper, StockLockLog> implements StockLockLogService {

    /**
     * 新增库存锁定记录
     */
    @Override
    public void newStockLockLog(StockLockLog stockLockLog) {
        LocalDateTime now = LocalDateTime.now();
        stockLockLog.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(stockLockLog);
    }
}
