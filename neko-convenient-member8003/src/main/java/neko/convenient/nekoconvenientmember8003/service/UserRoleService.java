package neko.convenient.nekoconvenientmember8003.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientmember8003.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    Page<UserRole> getUserRolesByQueryLimitedPage(QueryVo vo);

    List<UserRole> getAdminRoles();

    UserRole getUserRoleByRoleType(String roleType);
}
