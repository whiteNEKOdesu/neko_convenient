package neko.convenient.nekoconvenientproduct8005.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.entity.BrandInfo;
import neko.convenient.nekoconvenientproduct8005.service.BrandInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商店信息 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("brand_info")
public class BrandInfoController {
    @Resource
    private BrandInfoService brandInfoService;

    /**
     * 根据品牌名查询品牌信息
     */
    @GetMapping("brand_infos")
    public ResultObject<List<BrandInfo>> brandInfos(String brandName){
        return ResultObject.ok(brandInfoService.getBrandInfos(brandName));
    }
}
