package neko.convenient.nekoconvenientproduct8005.feign.member;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientproduct8005.to.WeightRoleRelationTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "neko-convenient-member")
public interface WeightRoleRelationFeignService {
    @PostMapping("weight_role_relation/relation_info_by_uid")
    public ResultObject<List<WeightRoleRelationTo>> relationInfoByUid(@RequestParam String uid);
}
