package neko.convenient.nekoconvenientthirdparty8006.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import neko.convenient.nekoconvenientthirdparty8006.config.OSSConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class OSSTest {
    @Resource
    private OSSClient ossClient;

    @Resource
    private OSSConfigProperties ossConfigProperties;

    @Value("${alibaba.cloud.access-key}")
    private String accessId;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Test
    public void deleteOSSFile(){
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConfigProperties.getBucket();
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "neko/neko_convenient/2023-06-10/1bb41b00-ea84-4940-838a-d8baf596c77f_1ad3ffe0-e81f-4439-8937-2fc22e2045ba_A400388D-AE79-4F23-8390-7D549A78D795.jpeg";

        try {
            // 删除文件。
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
    }
}
