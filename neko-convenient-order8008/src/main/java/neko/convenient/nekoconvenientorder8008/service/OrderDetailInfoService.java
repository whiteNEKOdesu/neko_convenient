package neko.convenient.nekoconvenientorder8008.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;

import java.util.List;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface OrderDetailInfoService extends IService<OrderDetailInfo> {
    Page<OrderDetailInfo> getUserSelfOrderDetailInfoByQueryLimitedPage(QueryVo vo);

    void userConfirmDelivered(String orderRecord);

    void updateCourierInfoByOrderRecords(List<String> orderRecords);
}
