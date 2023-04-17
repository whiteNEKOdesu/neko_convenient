package neko.convenient.nekoconvenientgateway8004.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import neko.convenient.nekoconvenientgateway8004.entity.Response;
import neko.convenient.nekoconvenientgateway8004.entity.ResultObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SATokenConfig {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                //拦截地址
                .addInclude("/member/user_role_relation/new_user_role_relation")
                .addInclude("/third_party/mail/send_register_mail")
                .addInclude("/member/weight_role_relation/relation_info_by_uid")
                .setAuth(obj -> {
                    StpUtil.checkRole("*");
                })
                //异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    return new ResultObject<Object>().setResponseStatus(Response.TOKEN_CHECK_ERROR)
                            .compact();
                });
    }
}
