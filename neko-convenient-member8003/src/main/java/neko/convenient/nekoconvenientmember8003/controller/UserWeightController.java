package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientmember8003.entity.UserWeight;
import neko.convenient.nekoconvenientmember8003.service.UserWeightService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("user_weight")
public class UserWeightController {
    @Resource
    private UserWeightService userWeightService;

    /**
     * 新增权限
     */
    @SaCheckRole(RoleType.ROOT)
    @SaCheckLogin
    @PutMapping("new_user_weight")
    public ResultObject<Object> newUserWeight(@RequestParam String weightType){
        userWeightService.newUserWeight(weightType);

        return ResultObject.ok();
    }

    /**
     * 分页查询权限信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("weight_info")
    public ResultObject<Page<UserWeight>> weightInfo(@RequestBody QueryVo vo){
        return ResultObject.ok(userWeightService.getUserWeightByQueryLimitedPage(vo));
    }

    /**
     * 获取指定roleId还未绑定权限信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("unbind_weight_info")
    public ResultObject<List<UserWeight>> unbindWeightInfo(@RequestParam Integer roleId){
        return ResultObject.ok(userWeightService.getUnbindWeightByRoleId(roleId));
    }
}
