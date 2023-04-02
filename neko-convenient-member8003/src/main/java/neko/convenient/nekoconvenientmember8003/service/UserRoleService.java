package neko.convenient.nekoconvenientmember8003.service;

import neko.convenient.nekoconvenientmember8003.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface UserRoleService extends IService<UserRole> {
    void newUserRole(String roleType);
}
