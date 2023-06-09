package neko.convenient.nekoconvenientmember8003.feign.thirdparty;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.THIRD_PARTY_SERVICE, contextId = "Mail")
public interface MailFeignService {
    @PostMapping("mail/send_register_mail")
    ResultObject<Object> sendRegisterMail(@RequestParam String receiver, @RequestParam String code);
}
