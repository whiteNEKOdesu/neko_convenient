package neko.convenient.nekoconvenientmember8003.mapper;

import neko.convenient.nekoconvenientmember8003.entity.UserWeight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface UserWeightMapper extends BaseMapper<UserWeight> {
    UserWeight getUserWeightByWeightType(String weightType);

    List<UserWeight> getUnbindUserWeightByRoleId(Integer roleId);
}
