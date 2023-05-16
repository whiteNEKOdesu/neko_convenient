package neko.convenient.nekoconvenientware8007.service.impl;

import neko.convenient.nekoconvenientware8007.entity.StockLockLog;
import neko.convenient.nekoconvenientware8007.mapper.StockLockLogMapper;
import neko.convenient.nekoconvenientware8007.service.StockLockLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientware8007.vo.LockProductInfoVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 修改库存锁定记录状态
     */
    @Override
    public void updateStockLockLogStatus(String stockLockLogId, Byte status, LocalDateTime updateTime) {
        this.baseMapper.updateStockLockLogStatus(stockLockLogId, status, updateTime);
    }

    /**
     * 根据orderRecord查询锁定商品信息
     */
    @Override
    public List<LockProductInfoVo> getSkuIdsByOrderRecord(String orderRecord) {
        return this.baseMapper.getLockProductInfoByOrderRecord(orderRecord);
    }

    /**
     * 根据orderRecord查询锁定商品详情信息
     */
    @Override
    public List<StockLockLog> getLockStockLockLogByOrderRecord(String orderRecord) {
        return this.baseMapper.getLockStockLockLogByOrderRecord(orderRecord);
    }
}
