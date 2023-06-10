package neko.convenient.nekoconvenientthirdparty8006.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientthirdparty8006.config.OSSCallbackConfig;
import neko.convenient.nekoconvenientthirdparty8006.config.OSSConfigProperties;
import neko.convenient.nekoconvenientthirdparty8006.service.OSSService;
import neko.convenient.nekoconvenientthirdparty8006.vo.OSSCallbackVo;
import neko.convenient.nekoconvenientthirdparty8006.vo.OSSVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Slf4j
public class OSSServiceImpl implements OSSService {
    @Resource
    private OSS ossClient;

    @Resource
    private OSSConfigProperties ossConfigProperties;

    @Value("${alibaba.cloud.access-key}")
    private String accessId;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    /**
     * 获取oss上传信息
     */
    @Override
    public OSSVo getPolicy() {
        // 填写Bucket名称，例如examplebucket。
        String bucket = ossConfigProperties.getBucket();
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;
        // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
        String callbackUrl = ossConfigProperties.getCallbackUrl();
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dir = ossConfigProperties.getDir() + "/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + "/";

        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        OSSCallbackConfig callbackConfig = new OSSCallbackConfig();
        callbackConfig.setCallbackUrl(callbackUrl);
        callbackConfig.setCallbackBody("{\"filename\":${object},\"mimeType\":${mimeType},\"size\":${size}}");

        OSSVo ossVo = new OSSVo();
        ossVo.setAccessId(accessId)
                .setPolicy(encodedPolicy)
                .setSignature(postSignature)
                .setDir(dir)
                .setHost(host)
                .setExpire(expireEndTime / 1000)
                .setCallbackUrl(callbackUrl)
                .setCallback(Base64.encode(JSONUtil.toJsonStr(callbackConfig).getBytes(StandardCharsets.UTF_8)));

        return ossVo;
    }

    /**
     * oss回调处理
     */
    @Override
    public void handleCallback(OSSCallbackVo vo) {
        log.info(vo.toString());
    }
}
