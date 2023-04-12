package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.hutool.json.JSONUtil;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientproduct8005.entity.AddressDict;
import neko.convenient.nekoconvenientproduct8005.mapper.AddressDictMapper;
import neko.convenient.nekoconvenientproduct8005.service.AddressDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 地址字典表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class AddressDictServiceImpl extends ServiceImpl<AddressDictMapper, AddressDict> implements AddressDictService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取层级地址信息
     */
    @Override
    public List<AddressDict> getLevelAddress() {
        String key = Constant.PRODUCT_REDIS_PREFIX + "level_address";
        String cache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(cache != null){
            return JSONUtil.toList(JSONUtil.parseArray(cache), AddressDict.class);
        }

        List<AddressDict> addressDicts = this.list();
        //找到父分类
        List<AddressDict> parentAddressDicts = addressDicts.stream().filter(Objects::nonNull)
                .filter(addressDict -> addressDict.getParentId().equals(0))
                .collect(Collectors.toList());

        //递归设置子地址信息
        List<AddressDict> result = parentAddressDicts.stream().peek(addressDict -> {
            addressDict.setChild(getLevelAddress(addressDict, addressDicts));
        }).collect(Collectors.toList());
        //缓存无数据，查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(result),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return result;
    }

    /**
     * 递归设置子地址信息
     */
    private List<AddressDict> getLevelAddress(AddressDict root, List<AddressDict> all){
        return all.stream().filter(addressDict -> root.getAddressId().equals(addressDict.getParentId()))
                .peek(addressDict -> addressDict.setChild(!getLevelAddress(addressDict, all).isEmpty() ? getLevelAddress(addressDict, all) : null))
                .collect(Collectors.toList());
    }
}
