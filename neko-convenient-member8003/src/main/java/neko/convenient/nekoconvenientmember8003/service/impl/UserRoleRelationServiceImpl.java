package neko.convenient.nekoconvenientmember8003.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.convenient.nekoconvenientmember8003.entity.UserRoleRelation;
import neko.convenient.nekoconvenientmember8003.mapper.UserRoleRelationMapper;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户，角色关系表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements UserRoleRelationService {

    @Override
    public List<Integer> getUserRoleIds(String uid) {
        return this.baseMapper.getRoleIdsByUid(uid);
    }

    @Override
    public int newRelation(String uid, Integer roleId) {
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setUid(uid)
                .setRoleId(roleId)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        return this.baseMapper.insert(userRoleRelation);
    }
}
