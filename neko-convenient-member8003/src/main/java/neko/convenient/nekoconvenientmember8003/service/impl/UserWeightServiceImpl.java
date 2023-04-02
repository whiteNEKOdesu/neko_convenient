package neko.convenient.nekoconvenientmember8003.service.impl;

import neko.convenient.nekoconvenientmember8003.entity.UserWeight;
import neko.convenient.nekoconvenientmember8003.mapper.UserWeightMapper;
import neko.convenient.nekoconvenientmember8003.service.UserWeightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class UserWeightServiceImpl extends ServiceImpl<UserWeightMapper, UserWeight> implements UserWeightService {

    /**
     * 新增权限
     */
    @Override
    public void newUserWeight(String weightType) {
        if(this.baseMapper.getUserWeightByWeightType(weightType) != null){
            throw new DuplicateKeyException("weightType重复");
        }

        UserWeight userWeight = new UserWeight();
        LocalDateTime now = LocalDateTime.now();
        userWeight.setWeightType(weightType)
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(userWeight);
    }
}
