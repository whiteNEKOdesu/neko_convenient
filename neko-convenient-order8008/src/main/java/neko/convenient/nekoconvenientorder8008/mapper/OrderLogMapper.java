package neko.convenient.nekoconvenientorder8008.mapper;

import neko.convenient.nekoconvenientorder8008.entity.OrderLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单生成记录表，用于解锁已取消订单库存 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Mapper
public interface OrderLogMapper extends BaseMapper<OrderLog> {

}
