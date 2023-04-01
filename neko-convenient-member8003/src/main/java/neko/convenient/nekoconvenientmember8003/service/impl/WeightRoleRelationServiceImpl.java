package neko.convenient.nekoconvenientmember8003.service.impl;

import neko.convenient.nekoconvenientmember8003.entity.WeightRoleRelation;
import neko.convenient.nekoconvenientmember8003.mapper.WeightRoleRelationMapper;
import neko.convenient.nekoconvenientmember8003.service.WeightRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限，角色关系表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class WeightRoleRelationServiceImpl extends ServiceImpl<WeightRoleRelationMapper, WeightRoleRelation> implements WeightRoleRelationService {

    @Override
    public List<WeightRoleRelation> getRelations(List<Integer> roleIds) {
        return this.baseMapper.getRelationsByRoleIds(roleIds);
    }
}
