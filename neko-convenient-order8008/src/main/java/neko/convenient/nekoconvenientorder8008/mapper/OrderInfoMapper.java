package neko.convenient.nekoconvenientorder8008.mapper;

import neko.convenient.nekoconvenientorder8008.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.convenient.nekoconvenientorder8008.vo.CourierOrderInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    List<CourierOrderInfoVo> getUnpickOrderInfoByQueryLimitedPage(Integer limited,
                                                                  Integer start,
                                                                  String queryWords);

    int getUnpickOrderInfoByQueryLimitedPageNumber(String queryWords);

    int updateCourierIdByOrderIds(String courierId,
                                  List<String> orderIds,
                                  LocalDateTime updateTime);

    void updateStatusToCourierConfirmByOrderId(String orderRecord, LocalDateTime updateTime);

    void updateStatusToUserConfirmByOrderId(String orderRecord, LocalDateTime updateTime);

    List<CourierOrderInfoVo> getUserSelfPickOrderInfoByQueryLimitedPage(Integer limited,
                                                                  Integer start,
                                                                  String queryWords,
                                                                  String courierId);

    int getUserSelfPickOrderInfoByQueryLimitedPageNumber(String queryWords, String courierId);
}
