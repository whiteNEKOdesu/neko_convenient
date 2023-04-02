package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.service.WeightRoleRelationService;
import neko.convenient.nekoconvenientmember8003.vo.NewWeightRoleRelationVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 权限，角色关系表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("weight_role_relation")
public class WeightRoleRelationController {
    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @SaCheckRole("admin")
    @PutMapping("new_relations")
    public ResultObject<Object> newRelations(@Validated @RequestBody NewWeightRoleRelationVo vo){
        weightRoleRelationService.newRelations(vo);

        return ResultObject.ok();
    }
}
