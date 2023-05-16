package neko.convenient.nekoconvenientorder8008.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;
import neko.convenient.nekoconvenientorder8008.service.OrderDetailInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 订单详情表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@RestController
@RequestMapping("order_detail_info")
public class OrderDetailInfoController {
    @Resource
    private OrderDetailInfoService orderDetailInfoService;

    /**
     * 分页查询用户自身购买商品信息
     */
    @PostMapping("user_self_order_detail_infos")
    public ResultObject<Page<OrderDetailInfo>> userSelfOrderDetailInfos(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(orderDetailInfoService.getUserSelfOrderDetailInfoByQueryLimitedPage(vo));
    }
}
