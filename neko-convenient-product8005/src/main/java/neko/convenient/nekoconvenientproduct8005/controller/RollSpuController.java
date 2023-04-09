package neko.convenient.nekoconvenientproduct8005.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.entity.RollSpu;
import neko.convenient.nekoconvenientproduct8005.service.RollSpuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
