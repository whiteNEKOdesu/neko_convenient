package neko.convenient.nekoconvenientware8007.mapper;

import neko.convenient.nekoconvenientware8007.entity.StockLockLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 库存锁定日志表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Mapper
public interface StockLockLogMapper extends BaseMapper<StockLockLog> {
    void updateStockLockLogStatus(String stockLockLogId,
                                  Byte status,
                                  LocalDateTime updateTime);
}
