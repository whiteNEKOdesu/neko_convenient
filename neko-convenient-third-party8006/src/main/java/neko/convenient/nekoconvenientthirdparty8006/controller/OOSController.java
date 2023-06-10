package neko.convenient.nekoconvenientthirdparty8006.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientthirdparty8006.service.OSSService;
import neko.convenient.nekoconvenientthirdparty8006.vo.OSSCallbackVo;
import neko.convenient.nekoconvenientthirdparty8006.vo.OSSVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("oss")
public class OOSController {
    @Resource
    private OSSService ossService;

    /**
     * 获取oss上传信息
     */
    @SaCheckLogin
    @GetMapping("policy")
    public ResultObject<OSSVo> policy() {
        return ResultObject.ok(ossService.getPolicy());
    }

    /**
     * oss回调处理
     */
    @PostMapping("callback")
    public ResultObject<Object> callback(@Validated @RequestBody OSSCallbackVo vo){
        ossService.handleCallback(vo);

        return ResultObject.ok();
    }
}
