package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.service.CourierApplyInfoService;
import neko.convenient.nekoconvenientmember8003.vo.ApplyCourierVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
