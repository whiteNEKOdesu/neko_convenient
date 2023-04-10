package neko.convenient.nekoconvenientmember8003.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.feign.oauth.GiteeOAuthFeignService;
import neko.convenient.nekoconvenientmember8003.service.OAuthService;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("oauth")
public class OAuthController {
    @Resource
    private OAuthService oAuthService;

    /**
     * gitee社交用户登录
     */
    @PostMapping("gitee_member_info")
    public ResultObject<MemberInfoVo> giteeMemberInfo(@RequestParam String code, HttpServletRequest request){
        return ResultObject.ok(oAuthService.giteeLogIn(code, request));
    }
}
