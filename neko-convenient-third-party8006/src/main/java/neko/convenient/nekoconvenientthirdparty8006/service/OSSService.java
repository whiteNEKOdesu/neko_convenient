package neko.convenient.nekoconvenientthirdparty8006.service;

import neko.convenient.nekoconvenientthirdparty8006.vo.OSSCallbackVo;
import neko.convenient.nekoconvenientthirdparty8006.vo.OSSVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OSSService {
    OSSVo getPolicy();

    void handleCallback(OSSCallbackVo vo);

    String uploadImage(MultipartFile file) throws IOException;

    void deleteFile(String ossFilePath);
}
