package neko.convenient.nekoconvenientproduct8005.mapper;

import neko.convenient.nekoconvenientproduct8005.entity.AddressDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地址字典表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface AddressDictMapper extends BaseMapper<AddressDict> {
    String getAddressInfoByAddressId(Integer addressId);
}
