package neko.convenient.nekoconvenientmember8003.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientmember8003.entity.CourierApplyInfo;
import neko.convenient.nekoconvenientmember8003.vo.AdminHandleCourierApplyVo;
import neko.convenient.nekoconvenientmember8003.vo.ApplyCourierVo;

/**
 * <p>
 * 快递员申请表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface CourierApplyInfoService extends IService<CourierApplyInfo> {
    void applyCourier(ApplyCourierVo vo);

    void handleApply(AdminHandleCourierApplyVo vo);
}
