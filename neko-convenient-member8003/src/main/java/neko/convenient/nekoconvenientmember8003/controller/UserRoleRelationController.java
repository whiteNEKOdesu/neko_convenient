package neko.convenient.nekoconvenientmember8003.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import neko.convenient.nekoconvenientmember8003.vo.NewUserRoleRelationVo;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户，角色关系表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("user_role_relation")
public class UserRoleRelationController {
    @Resource
    private UserRoleRelationService userRoleRelationService;

    /**
     * 批量新增uid，角色关系，内部微服务调用
     */
    @PutMapping("new_user_role_relation")
    public ResultObject<Object> newUserRoleRelation(@RequestBody NewUserRoleRelationVo vo){
        userRoleRelationService.newRelations(vo.getUid(), vo.getRoleIds());

        return ResultObject.ok();
    }
}
