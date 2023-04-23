package neko.convenient.nekoconvenientware8007.feign.member;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientware8007.to.WeightRoleRelationTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServiceName.MEMBER_SERVICE, contextId = "WeightRoleRelation")
public interface WeightRoleRelationFeignService {
    @PostMapping("weight_role_relation/relation_info_by_uid")
    ResultObject<List<WeightRoleRelationTo>> relationInfoByUid(@RequestParam String uid);
}
