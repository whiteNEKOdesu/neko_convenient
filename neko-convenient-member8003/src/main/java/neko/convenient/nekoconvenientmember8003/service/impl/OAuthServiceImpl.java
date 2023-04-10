package neko.convenient.nekoconvenientmember8003.service.impl;

import neko.convenient.nekoconvenientcommonbase.utils.exception.OAuthException;
import neko.convenient.nekoconvenientmember8003.config.GiteeOAuthConfigProperties;
import neko.convenient.nekoconvenientmember8003.feign.oauth.GiteeOAuthFeignService;
import neko.convenient.nekoconvenientmember8003.service.MemberInfoService;
import neko.convenient.nekoconvenientmember8003.service.OAuthService;
import neko.convenient.nekoconvenientmember8003.vo.GiteeMemberVo;
import neko.convenient.nekoconvenientmember8003.vo.GiteeTokenInfoVo;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class OAuthServiceImpl implements OAuthService {
    @Resource
    private GiteeOAuthFeignService giteeOAuthFeignService;

    @Resource
    private MemberInfoService memberInfoService;

    @Resource
    private GiteeOAuthConfigProperties giteeOAuthConfigProperties;

    /**
     * gitee社交用户登录
     */
    @Override
    public MemberInfoVo giteeLogIn(String code, HttpServletRequest request) {
        GiteeTokenInfoVo giteeTokenInfoVo = giteeOAuthFeignService.tokenInfo(giteeOAuthConfigProperties.getClientId(),
                giteeOAuthConfigProperties.getClientSecret(),
                giteeOAuthConfigProperties.getGrantType(),
                giteeOAuthConfigProperties.getRedirectUri(),
                code);

        if(giteeTokenInfoVo == null){
            throw new OAuthException("gitee社交登录验证错误");
        }

        GiteeMemberVo giteeMemberVo = giteeOAuthFeignService.socialUser(giteeTokenInfoVo.getAccess_token());
        if(giteeMemberVo == null){
            throw new OAuthException("gitee社交登录验证错误");
        }

        return memberInfoService.oAuthMemberLogIn(giteeMemberVo.getLogin(),
                "gitee",
                giteeMemberVo.getId(),
                request);
    }
}
