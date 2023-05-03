package neko.convenient.nekoconvenientorder8008.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientorder8008.entity.ReceiveAddress;
import neko.convenient.nekoconvenientorder8008.vo.NewReceiveAddressVo;

import java.util.List;

/**
 * <p>
 * 收货地址表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface ReceiveAddressService extends IService<ReceiveAddress> {
    void newReceiveAddress(NewReceiveAddressVo vo);

    List<ReceiveAddress> getUserSelfAddressInfos();

    ReceiveAddress getUserSelfAddressInfoByReceiveAddressId(String receiveAddressId);
}
