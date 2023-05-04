package neko.convenient.nekoconvenientorder8008.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientorder8008.entity.ReceiveAddress;
import neko.convenient.nekoconvenientorder8008.service.ReceiveAddressService;
import neko.convenient.nekoconvenientorder8008.vo.NewReceiveAddressVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@RestController
@RequestMapping("receive_address")
public class ReceiveAddressController {
    @Resource
    private ReceiveAddressService receiveAddressService;

    /**
     * 新增收货地址信息
     */
    @SaCheckLogin
    @PutMapping("new_receive_address")
    public ResultObject<Object> newReceiveAddress(@Validated @RequestBody NewReceiveAddressVo vo){
        receiveAddressService.newReceiveAddress(vo);

        return ResultObject.ok();
    }

    /**
     * 查询用户自身收货地址信息
     */
    @SaCheckLogin
    @PostMapping("address_infos")
    public ResultObject<List<ReceiveAddress>> addressInfos(){
        return ResultObject.ok(receiveAddressService.getUserSelfAddressInfos());
    }

    /**
     * 根据receiveAddressId获取用户自身收货地址信息
     */
    @SaCheckLogin
    @PostMapping("address_info")
    public ResultObject<ReceiveAddress> addressInfo(@RequestParam String receiveAddressId){
        return ResultObject.ok(receiveAddressService.getUserSelfAddressInfoByReceiveAddressId(receiveAddressId));
    }
}
