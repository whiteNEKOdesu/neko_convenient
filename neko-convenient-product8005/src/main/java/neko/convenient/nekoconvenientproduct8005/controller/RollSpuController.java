package neko.convenient.nekoconvenientproduct8005.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientproduct8005.entity.RollSpu;
import neko.convenient.nekoconvenientproduct8005.service.RollSpuService;
import neko.convenient.nekoconvenientproduct8005.vo.NewRollSpuVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品轮播图表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("roll_spu")
public class RollSpuController {
    @Resource
    private RollSpuService rollSpuService;

    /**
     * 获取商品轮播图信息
     */
    @GetMapping("roll_spu_info")
    public ResultObject<List<RollSpu>> rollSpuInfo(){
        return ResultObject.ok(rollSpuService.getRollSpuInfo());
    }

    /**
     * 管理员添加商品轮播图信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @PutMapping("new_roll_spu")
    public ResultObject<Object> newRollSpu(@Validated @RequestBody NewRollSpuVo vo){
        rollSpuService.newRollSpu(vo);

        return ResultObject.ok();
    }

    /**
     * 管理员根据rollId删除商品轮播图信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @SaCheckLogin
    @DeleteMapping("delete_roll_spu")
    public ResultObject<Object> deleteRollSpu(@RequestParam Integer rollId){
        rollSpuService.deleteRollSpuByRollId(rollId);

        return ResultObject.ok();
    }
}
