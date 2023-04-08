package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import neko.convenient.nekoconvenientproduct8005.service.CategoryInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.CategoryInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("category_Info")
public class CategoryInfoController {
    @Resource
    private CategoryInfoService categoryInfoService;

    /**
     * 获取层级商品分类信息
     */
    @GetMapping("level_category_info")
    public ResultObject<List<CategoryInfo>> levelCategoryInfo(){
        return ResultObject.ok(categoryInfoService.getLevelCategory());
    }

    /**
     * 新增商品分类信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PutMapping("new_category_info")
    public ResultObject<Object> newCategoryInfo(@Validated @RequestBody CategoryInfoVo vo){
        categoryInfoService.newCategoryInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 删除叶节点商品分类信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @DeleteMapping("delete_leaf_category_info")
    public ResultObject<Object> deleteLeafCategoryInfo(@RequestParam Integer categoryId){
        categoryInfoService.deleteLeafCategoryInfo(categoryId);

        return ResultObject.ok();
    }
}
