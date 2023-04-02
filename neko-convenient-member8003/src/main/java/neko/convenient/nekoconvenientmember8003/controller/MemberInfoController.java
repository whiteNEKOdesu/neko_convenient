package neko.convenient.nekoconvenientmember8003.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.service.MemberInfoService;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;
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
    public ResultObject<MemberInfoVo> logIn(@RequestParam String userName, @RequestParam String userPassword, HttpServletRequest request){
        return memberInfoService.logIn(userName, userPassword, request);
    }

    @PostMapping("register")
    public ResultObject<Integer> register(@RequestParam String userName, @RequestParam String userPassword){
        return ResultObject.ok(memberInfoService.register(userName, userPassword));
    }

    @GetMapping("user_name_is_repeat")
    public ResultObject<Boolean> userNameIsRepeat(@RequestParam String userName){
        return ResultObject.ok(memberInfoService.userNameIsRepeat(userName));
    }
}
