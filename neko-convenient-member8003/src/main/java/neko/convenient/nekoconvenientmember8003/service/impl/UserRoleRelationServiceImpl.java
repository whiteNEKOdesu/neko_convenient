package neko.convenient.nekoconvenientmember8003.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientmember8003.entity.UserRoleRelation;
import neko.convenient.nekoconvenientmember8003.mapper.UserRoleRelationMapper;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 获取用户角色id信息
     */
    @Override
    public List<Integer> getUserRoleIds(String uid) {
        return this.baseMapper.getRoleIdsByUid(uid);
    }

    /**
     * 新增uid，角色关系
     */
    @Override
    public int newRelation(String uid, Integer roleId) {
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setUid(uid)
                .setRoleId(roleId)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        return this.baseMapper.insert(userRoleRelation);
    }

    /**
     * 批量新增uid，角色关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newRelations(String uid, List<Integer> roleIds) {
        LocalDateTime now = LocalDateTime.now();
        List<UserRoleRelation> relations = roleIds.stream().filter(Objects::nonNull)
                .distinct()
                .map(r -> new UserRoleRelation().setUid(uid)
                        .setRoleId(r)
                        .setCreateTime(now)
                        .setUpdateTime(now))
                .collect(Collectors.toList());

        this.saveBatch(relations);
    }
}
