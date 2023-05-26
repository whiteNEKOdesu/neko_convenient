package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientmember8003.entity.CourierApplyInfo;
import neko.convenient.nekoconvenientmember8003.service.CourierApplyInfoService;
import neko.convenient.nekoconvenientmember8003.vo.AdminHandleCourierApplyVo;
import neko.convenient.nekoconvenientmember8003.vo.ApplyCourierVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("courier_apply_info")
public class CourierApplyInfoController {
    @Resource
    private CourierApplyInfoService courierApplyInfoService;

    /**
     * 添加申请快递员信息
     */
    @SaCheckLogin
    @PutMapping("apply_courier")
    public ResultObject<Object> applyCourier(@Validated @RequestBody ApplyCourierVo vo){
        courierApplyInfoService.applyCourier(vo);

        return ResultObject.ok();
    }

    /**
     * 管理员处理快递员申请信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("handle_apply")
    public ResultObject<Object> handleApply(@Validated @RequestBody AdminHandleCourierApplyVo vo){
        courierApplyInfoService.handleApply(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询未处理快递员申请信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("unhandled_apply_infos")
    public ResultObject<Page<CourierApplyInfo>> unhandledApplyInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(courierApplyInfoService.getCourierApplyInfoByQueryLimitedPage(vo));
    }

    /**
     * 分页查询用户自身快递员申请信息
     */
    @SaCheckLogin
    @PostMapping("user_self_apply_infos")
    public ResultObject<Page<CourierApplyInfo>> userSelfApplyInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(courierApplyInfoService.getUserSelfCourierApplyInfoByQueryLimitedPage(vo));
    }
}
