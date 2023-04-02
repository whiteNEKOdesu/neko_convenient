package neko.convenient.nekoconvenientmember8003.service.impl;

import neko.convenient.nekoconvenientmember8003.entity.AdminLogInLog;
import neko.convenient.nekoconvenientmember8003.mapper.AdminLogInLogMapper;
import neko.convenient.nekoconvenientmember8003.service.AdminLogInLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 管理员登录记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class AdminLogInLogServiceImpl extends ServiceImpl<AdminLogInLogMapper, AdminLogInLog> implements AdminLogInLogService {

    @Override
    public int newLog(String uid, String ip, Boolean isLogIn) {
        AdminLogInLog adminLogInLog = new AdminLogInLog();
        adminLogInLog.setAdminId(uid)
                .setIp(ip)
                .setIsLogIn(isLogIn)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        return this.baseMapper.insert(adminLogInLog);
    }
}
