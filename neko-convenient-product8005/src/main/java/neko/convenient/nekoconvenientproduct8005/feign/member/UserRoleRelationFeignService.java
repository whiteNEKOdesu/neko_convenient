package neko.convenient.nekoconvenientproduct8005.feign.member;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.to.NewUserRoleRelationTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "neko-convenient-member", contextId = "UserRoleRelation")
public interface UserRoleRelationFeignService {
    @PutMapping("user_role_relation/new_user_role_relation")
    ResultObject<Object> newUserRoleRelation(@RequestBody NewUserRoleRelationTo to);
}
