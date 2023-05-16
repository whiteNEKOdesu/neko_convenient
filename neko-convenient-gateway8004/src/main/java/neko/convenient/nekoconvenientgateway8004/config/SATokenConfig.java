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
                .addInclude("/product/sku_info/sku_id_market_info")
                .addInclude("/product/sku_info/product_infos")
                .addInclude("/ware/ware_info/lock_stock")
                .addInclude("/order/order_log/preorder_status")
                .addInclude("/ware/ware_info/unlock_stock")
                .addInclude("/ware/stock_lock_log/order_record_sku_id_infos")
                .addInclude("/product/sku_info/order_detail_infos")
                .addInclude("/ware/ware_info/confirm_lock_stock_pay")
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
