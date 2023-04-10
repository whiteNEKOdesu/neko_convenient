package neko.convenient.nekoconvenientmember8003.feign.oauth;

import neko.convenient.nekoconvenientmember8003.vo.GiteeMemberVo;
import neko.convenient.nekoconvenientmember8003.vo.GiteeTokenInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://gitee.com", name = "gitee")
public interface GiteeOAuthFeignService {
    @PostMapping("oauth/token")
    GiteeTokenInfoVo tokenInfo(@RequestParam String client_id,
                                      @RequestParam String client_secret,
                                      @RequestParam String grant_type,
                                      @RequestParam String redirect_uri,
                                      @RequestParam String code);

    @GetMapping("api/v5/user")
    GiteeMemberVo socialUser(@RequestParam String access_token);
}
