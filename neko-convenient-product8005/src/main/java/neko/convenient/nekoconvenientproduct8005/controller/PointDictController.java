package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.PointDict;
import neko.convenient.nekoconvenientproduct8005.service.PointDictService;
import neko.convenient.nekoconvenientproduct8005.vo.NewPointDictVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 积分字典表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("point_dict")
public class PointDictController {
    @Resource
    private PointDictService pointDictService;

    /**
     * 添加积分规则
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PutMapping("new_point_dict")
    public ResultObject<Object> newPointDict(@Validated @RequestBody NewPointDictVo vo){
        pointDictService.newPointDict(vo);

        return ResultObject.ok();
    }

    /**
     * 获取所有积分规则
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("point_dict_infos")
    public ResultObject<List<PointDict>> pointDictInfos(){
        return ResultObject.ok(pointDictService.getPointDictInfos());
    }

    /**
     * 删除已有最大价格积分规则
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @DeleteMapping("delete_highest_point_dict")
    public ResultObject<Object> deleteHighestPointDict(){
        pointDictService.deleteHighestPricePointDict();

        return ResultObject.ok();
    }

    /**
     * 修改已有最大价格积分规则
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PostMapping("update_highest_point_dict")
    public ResultObject<Object> updateHighestPointDict(@Validated @RequestBody NewPointDictVo vo){
        pointDictService.updateHighestPricePointDict(vo);

        return ResultObject.ok();
    }

    /**
     * 根据价格获取积分，建议只提供给微服务远程调用
     */
    @PostMapping("price_point")
    public ResultObject<Integer> pricePoint(@RequestParam BigDecimal price){
        return ResultObject.ok(pointDictService.getPointByPrice(price));
    }
}
