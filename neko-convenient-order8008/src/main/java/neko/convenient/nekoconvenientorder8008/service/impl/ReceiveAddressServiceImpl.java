package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ProductServiceException;
import neko.convenient.nekoconvenientorder8008.entity.ReceiveAddress;
import neko.convenient.nekoconvenientorder8008.feign.product.AddressDictFeignService;
import neko.convenient.nekoconvenientorder8008.mapper.ReceiveAddressMapper;
import neko.convenient.nekoconvenientorder8008.service.ReceiveAddressService;
import neko.convenient.nekoconvenientorder8008.vo.NewReceiveAddressVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Service
public class ReceiveAddressServiceImpl extends ServiceImpl<ReceiveAddressMapper, ReceiveAddress> implements ReceiveAddressService {
    @Resource
    private AddressDictFeignService addressDictFeignService;

    /**
     * 新增收货地址信息
     */
    @Override
    public void newReceiveAddress(NewReceiveAddressVo vo) {
        ResultObject<String> r = addressDictFeignService.addressInfo(vo.getAddressId());
        if(r.getResponseCode().equals(17)){
            throw new NoSuchResultException("地址信息不存在");
        }else if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }

        ReceiveAddress receiveAddress = new ReceiveAddress();
        LocalDateTime now = LocalDateTime.now();
        receiveAddress.setUid(StpUtil.getLoginId().toString())
                .setAddressId(vo.getAddressId())
                .setAddressName(r.getResult() + vo.getAddressName())
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(receiveAddress);
    }

    /**
     * 查询用户自身收货地址信息
     */
    @Override
    public List<ReceiveAddress> getUserSelfAddressInfos() {
        return this.lambdaQuery()
                .eq(ReceiveAddress::getUid, StpUtil.getLoginId().toString())
                .list();
    }

    /**
     * 根据receiveAddressId获取用户自身收货地址信息
     */
    @Override
    public ReceiveAddress getUserSelfAddressInfoByReceiveAddressId(String receiveAddressId) {
        ReceiveAddress receiveAddress = this.baseMapper.selectById(receiveAddressId);
        if(receiveAddress == null || !receiveAddress.getUid().equals(StpUtil.getLoginId().toString())){
            throw new NotPermissionException("权限不足");
        }

        return receiveAddress;
    }
}
