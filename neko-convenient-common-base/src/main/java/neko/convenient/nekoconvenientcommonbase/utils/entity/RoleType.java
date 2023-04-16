package neko.convenient.nekoconvenientcommonbase.utils.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoleType {
    public static final String ROOT = "root";

    public static final String ADMIN = "admin";

    public static final String MARKET = "market";

    /**
     * 微服务调用角色
     */
    public static final String NEKO_CONVENIENT_SERVICE = "neko_convenient_service";
}
