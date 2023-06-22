package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.service.MemberInfoService;
import neko.convenient.nekoconvenientmember8003.vo.AddMemberPointVo;
import neko.convenient.nekoconvenientmember8003.vo.LogInVo;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;
import neko.convenient.nekoconvenientmember8003.vo.UpdateUserPasswordVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("member_info")
public class MemberInfoController {
    @Resource
    private MemberInfoService memberInfoService;

    @PostMapping("log_in")
    public ResultObject<MemberInfoVo> logIn(@Validated @RequestBody LogInVo vo, HttpServletRequest request){
        return memberInfoService.logIn(vo, request);
    }

    @PostMapping("register")
    public ResultObject<Integer> register(@RequestParam String userName,
                                          @RequestParam String userPassword,
                                          @RequestParam String email,
                                          @RequestParam String code){
        return ResultObject.ok(memberInfoService.register(userName, userPassword, email, code));
    }

    @GetMapping("user_name_is_repeat")
    public ResultObject<Boolean> userNameIsRepeat(@RequestParam String userName){
        return ResultObject.ok(memberInfoService.userNameIsRepeat(userName));
    }

    @PostMapping("send_register_mail")
    public ResultObject<Object> sendRegisterMail(@RequestParam String mail){
        memberInfoService.sendRegisterCode(mail);
        return ResultObject.ok();
    }

    @SaCheckLogin
    @PostMapping("update_user_password")
    public ResultObject<Object> updateUserPassword(@Validated @RequestBody UpdateUserPasswordVo vo){
        memberInfoService.updateUserPassword(vo);

        return ResultObject.ok();
    }

    @SaCheckLogin
    @PostMapping("update_user_name")
    public ResultObject<Object> updateUserName(@RequestParam String userName){
        memberInfoService.updateUserName(userName);

        return ResultObject.ok();
    }

    /**
     * 添加用户积分，建议只提供给微服务远程调用
     */
    @PostMapping("add_point")
    public ResultObject<Object> addPoint(@Validated @RequestBody AddMemberPointVo vo){
        memberInfoService.addPoint(vo);

        return ResultObject.ok();
    }

    /**
     * 根据uid获取真实姓名，建议只提供给微服务远程调用
     */
    @PostMapping("real_name_info")
    public ResultObject<String> realNameInfo(@RequestParam String uid){
        return ResultObject.ok(memberInfoService.getRealNameByUid(uid));
    }
}
