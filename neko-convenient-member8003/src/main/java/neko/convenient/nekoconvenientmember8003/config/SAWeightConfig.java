package neko.convenient.nekoconvenientmember8003.config;

import cn.dev33.satoken.stp.StpInterface;
import neko.convenient.nekoconvenientmember8003.entity.WeightRoleRelation;
import neko.convenient.nekoconvenientmember8003.service.WeightRoleRelationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 权限设置配置
 */
@Component
public class SAWeightConfig implements StpInterface {
    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return weightRoleRelationService.getRelations(o.toString()).stream().filter(Objects::nonNull)
                .map(WeightRoleRelation::getWeightType)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return weightRoleRelationService.getRelations(o.toString()).stream().filter(Objects::nonNull)
                .map(WeightRoleRelation::getRoleType)
                .distinct()
                .collect(Collectors.toList());
    }
}
