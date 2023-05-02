package neko.convenient.nekoconvenientmember8003.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientmember8003.entity.MemberLevelDict;
import neko.convenient.nekoconvenientmember8003.service.MemberLevelDictService;
import neko.convenient.nekoconvenientmember8003.vo.NewMemberLevelVo;
import neko.convenient.nekoconvenientmember8003.vo.UpdateMemberLevelVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户等级字典表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("member_level_dict")
public class MemberLevelDictController {
    @Resource
    private MemberLevelDictService memberLevelDictService;

    /**
     * 新增用户等级
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PutMapping("new_level")
    public ResultObject<Object> newLevel(@Validated @RequestBody NewMemberLevelVo vo){
        memberLevelDictService.newMemberLevel(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询用户等级信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("level_infos")
    public ResultObject<Page<MemberLevelDict>> levelInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(memberLevelDictService.getMemberLevelDictByQueryLimitedPage(vo));
    }

    /**
     * 修改用户等级信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("update_level_info")
    public ResultObject<Object> updateLevelInfo(@Validated @RequestBody UpdateMemberLevelVo vo){
        memberLevelDictService.updateMemberLevelDict(vo);

        return ResultObject.ok();
    }
}
