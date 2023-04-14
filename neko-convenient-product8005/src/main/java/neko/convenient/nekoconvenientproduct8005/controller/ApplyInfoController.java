package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.ApplyInfo;
import neko.convenient.nekoconvenientproduct8005.service.ApplyInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.AdminApplyInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.ApplyInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 品牌申请表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("apply_info")
public class ApplyInfoController {
    @Resource
    private ApplyInfoService applyInfoService;

    /**
     * 申请连锁店品牌
     */
    @SaCheckLogin
    @PutMapping("apply_brand")
    public ResultObject<Object> applyBrand(@Validated @RequestBody ApplyInfoVo vo){
        applyInfoService.applyBrand(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询申请信息
     */
    @SaCheckLogin
    @PostMapping("apply_status_info")
    public ResultObject<Page<ApplyInfo>> applyStatusInfo(@RequestBody QueryVo vo){
        return ResultObject.ok(applyInfoService.getUserSelfApplyInfoByQueryLimitedPage(vo));
    }

    /**
     * 分页查询所有用户申请信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("apply_infos")
    public ResultObject<Page<ApplyInfo>> applyInfos(@RequestBody QueryVo vo){
        return ResultObject.ok(applyInfoService.getApplyInfoByQueryLimitedPage(vo));
    }

    /**
     * 管理员处理申请信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("handle_apply")
    public ResultObject<Object> handleApply(@Validated @RequestBody AdminApplyInfoVo vo){
        applyInfoService.handleApply(vo);

        return ResultObject.ok();
    }
}
