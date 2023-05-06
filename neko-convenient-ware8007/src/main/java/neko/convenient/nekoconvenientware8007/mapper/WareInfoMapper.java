package neko.convenient.nekoconvenientware8007.mapper;

import neko.convenient.nekoconvenientware8007.entity.WareInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * sku库存信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Mapper
public interface WareInfoMapper extends BaseMapper<WareInfo> {
    void updateStockByWareId(String wareId,
                             Integer updateStockNumber,
                             LocalDateTime updateTime);

    int lockStock(String wareId,
                   Integer todoLockNumber,
                   LocalDateTime updateTime);
}
