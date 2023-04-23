package neko.convenient.nekoconvenientware8007.service.impl;

import neko.convenient.nekoconvenientware8007.entity.StockUpdateLog;
import neko.convenient.nekoconvenientware8007.mapper.StockUpdateLogMapper;
import neko.convenient.nekoconvenientware8007.service.StockUpdateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存更新日志表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Service
public class StockUpdateLogServiceImpl extends ServiceImpl<StockUpdateLogMapper, StockUpdateLog> implements StockUpdateLogService {

    /**
     * 新增库存更新记录
     */
    @Override
    public void newLog(StockUpdateLog stockUpdateLog) {
        if(stockUpdateLog != null){
            this.baseMapper.insert(stockUpdateLog);
        }
    }
}
