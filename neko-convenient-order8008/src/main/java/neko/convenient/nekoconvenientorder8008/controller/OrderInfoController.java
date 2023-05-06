package neko.convenient.nekoconvenientorder8008.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientorder8008.service.OrderInfoService;
import neko.convenient.nekoconvenientorder8008.vo.NewOrderVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@RestController
@RequestMapping("order_info")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 新增订单
     */
    @PutMapping("new_order")
    public ResultObject<Object> newOrder(@Validated @RequestBody NewOrderVo vo){
        orderInfoService.newOrder(vo);

        return ResultObject.ok();
    }
}
