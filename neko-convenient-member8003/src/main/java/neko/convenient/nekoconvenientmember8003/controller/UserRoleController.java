package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.entity.UserRole;
import neko.convenient.nekoconvenientmember8003.service.UserRoleService;
import neko.convenient.nekoconvenientmember8003.vo.QueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("user_role")
public class UserRoleController {
    @Resource
    private UserRoleService userRoleService;

    /**
     * 新增角色
     */
    @SaCheckRole("admin")
    @SaCheckLogin
    @PutMapping("new_user_role")
    public ResultObject<Object> newUserRole(String roleType){
        userRoleService.newUserRole(roleType);

        return ResultObject.ok();
    }

    /**
     * 分页查询角色信息
     */
    @SaCheckRole("root")
    @SaCheckLogin
    @PostMapping("role_info")
    public ResultObject<Page<UserRole>> roleInfo(@RequestBody QueryVo vo){
        return ResultObject.ok(userRoleService.getUserRolesByQueryLimitedPage(vo));
    }
}
