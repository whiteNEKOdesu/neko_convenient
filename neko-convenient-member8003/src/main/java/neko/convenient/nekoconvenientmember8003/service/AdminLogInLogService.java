package neko.convenient.nekoconvenientmember8003.service;

import neko.convenient.nekoconvenientmember8003.entity.AdminLogInLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理员登录记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface AdminLogInLogService extends IService<AdminLogInLog> {
    int newLog(String uid, String ip, Boolean isLogIn);
}
