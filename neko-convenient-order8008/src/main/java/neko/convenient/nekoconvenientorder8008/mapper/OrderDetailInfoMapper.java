package neko.convenient.nekoconvenientorder8008.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单详情表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Mapper
public interface OrderDetailInfoMapper extends BaseMapper<OrderDetailInfo> {
    void updateStatusToDeliveredByOrderRecord(String orderRecord, LocalDateTime updateTime);
}
