package neko.convenient.nekoconvenientmember8003.mapper;

import neko.convenient.nekoconvenientmember8003.entity.WeightRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限，角色关系表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface WeightRoleRelationMapper extends BaseMapper<WeightRoleRelation> {
    List<WeightRoleRelation> getRelationsByRoleIds(List<Integer> roleIds);
}
