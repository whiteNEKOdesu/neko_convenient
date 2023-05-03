package neko.convenient.nekoconvenientproduct8005.service;

import neko.convenient.nekoconvenientproduct8005.entity.AddressDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地址字典表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface AddressDictService extends IService<AddressDict> {
    List<AddressDict> getLevelAddress();

    String getAddressByAddressId(Integer addressId);
}
