package neko.convenient.nekoconvenientmember8003.service;

import neko.convenient.nekoconvenientmember8003.entity.MemberLogInLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface MemberLogInLogService extends IService<MemberLogInLog> {
    int newLog(String uid, String ip, Boolean isLogIn);
}
