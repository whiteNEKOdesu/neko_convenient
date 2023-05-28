package neko.convenient.nekoconvenientorder8008.feign.member;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import neko.convenient.nekoconvenientorder8008.to.AddMemberPointTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.MEMBER_SERVICE, contextId = "MemberInfo")
public interface MemberInfoFeignService {
    @PostMapping("member_info/add_point")
    ResultObject<Object> addPoint(@RequestBody AddMemberPointTo to);

    @PostMapping("member_info/real_name_info")
    ResultObject<String> realNameInfo(@RequestParam String uid);
}
