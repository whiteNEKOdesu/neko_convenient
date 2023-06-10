package neko.convenient.nekoconvenientthirdparty8006.service;

import neko.convenient.nekoconvenientthirdparty8006.vo.OSSCallbackVo;
import neko.convenient.nekoconvenientthirdparty8006.vo.OSSVo;

public interface OSSService {
    OSSVo getPolicy();

    void handleCallback(OSSCallbackVo vo);
}
