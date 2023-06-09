package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientmember8003.service.AdminInfoService;
import neko.convenient.nekoconvenientmember8003.vo.AdminInfoVo;
import neko.convenient.nekoconvenientmember8003.vo.LogInVo;
import neko.convenient.nekoconvenientmember8003.vo.NewAdminInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("admin_info")
public class AdminInfoController {
    @Resource
    private AdminInfoService adminInfoService;

    @PostMapping("log_in")
    public ResultObject<AdminInfoVo> logIn(@Validated @RequestBody LogInVo vo, HttpServletRequest request){
        return adminInfoService.logIn(vo, request);
    }

    /**
     * 新增管理员
     */
    @SaCheckRole(RoleType.ROOT)
    @SaCheckLogin
    @PutMapping("new_admin")
    public ResultObject<Object> newAdmin(@Validated @RequestBody NewAdminInfoVo vo){
        adminInfoService.newAdmin(vo);

        return ResultObject.ok();
    }

    @SaCheckRole(RoleType.ROOT)
    @SaCheckLogin
    @GetMapping("user_name_is_repeat")
    public ResultObject<Boolean> userNameIsRepeat(@RequestParam String userName){
        return ResultObject.ok(adminInfoService.userNameIsRepeat(userName));
    }
}
