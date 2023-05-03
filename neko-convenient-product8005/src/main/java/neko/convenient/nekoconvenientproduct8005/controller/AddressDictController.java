package neko.convenient.nekoconvenientproduct8005.controller;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.entity.AddressDict;
import neko.convenient.nekoconvenientproduct8005.service.AddressDictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 地址字典表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@RestController
@RequestMapping("address_dict")
public class AddressDictController {
    @Resource
    private AddressDictService addressDictService;

    /**
     * 获取层级地址信息
     */
    @GetMapping("level_address_info")
    public ResultObject<List<AddressDict>> levelAddressInfo(){
        return ResultObject.ok(addressDictService.getLevelAddress());
    }

    /**
     * 根据addressId获取完整地址信息
     */
    @GetMapping("address_info")
    public ResultObject<String> addressInfo(@RequestParam Integer addressId){
        return ResultObject.ok(addressDictService.getAddressByAddressId(addressId));
    }
}
