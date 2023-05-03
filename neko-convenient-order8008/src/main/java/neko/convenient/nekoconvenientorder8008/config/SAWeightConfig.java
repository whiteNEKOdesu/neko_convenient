package neko.convenient.nekoconvenientorder8008.config;

import cn.dev33.satoken.stp.StpInterface;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.MemberServiceException;
import neko.convenient.nekoconvenientorder8008.feign.member.WeightRoleRelationFeignService;
import neko.convenient.nekoconvenientorder8008.to.WeightRoleRelationTo;
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
    private WeightRoleRelationFeignService weightRoleRelationFeignService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return getWeightTypesByUid(o.toString());
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return getRoleTypesByUid(o.toString());
    }

    private List<String> getWeightTypesByUid(String uid){
        ResultObject<List<WeightRoleRelationTo>> r = weightRoleRelationFeignService.relationInfoByUid(uid);
        if(r.getResponseCode() != 200){
            throw new MemberServiceException("member微服务远程调用错误");
        }

        return r.getResult().stream().filter(Objects::nonNull)
                .map(WeightRoleRelationTo::getWeightType)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getRoleTypesByUid(String uid){
        ResultObject<List<WeightRoleRelationTo>> r = weightRoleRelationFeignService.relationInfoByUid(uid);
        if(r.getResponseCode() != 200){
            throw new MemberServiceException("member微服务远程调用错误");
        }

        return r.getResult().stream().filter(Objects::nonNull)
                .map(WeightRoleRelationTo::getRoleType)
                .distinct()
                .collect(Collectors.toList());
    }
}
