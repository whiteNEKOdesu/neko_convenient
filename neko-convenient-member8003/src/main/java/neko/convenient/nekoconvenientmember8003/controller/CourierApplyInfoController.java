package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
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
}
