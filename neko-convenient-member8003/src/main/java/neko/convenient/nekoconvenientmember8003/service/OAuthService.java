package neko.convenient.nekoconvenientmember8003.service;

import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;

import javax.servlet.http.HttpServletRequest;

public interface OAuthService {
    MemberInfoVo giteeLogIn(String code, HttpServletRequest request);
}
