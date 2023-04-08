package neko.convenient.nekoconvenientmember8003.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientmember8003.entity.UserWeight;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface UserWeightService extends IService<UserWeight> {
    void newUserWeight(String weightType);

    Page<UserWeight> getUserWeightByQueryLimitedPage(QueryVo vo);

    List<UserWeight> getUnbindWeightByRoleId(Integer roleId);
}
