package neko.convenient.nekoconvenientproduct8005.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import neko.convenient.nekoconvenientproduct8005.service.CategoryInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
