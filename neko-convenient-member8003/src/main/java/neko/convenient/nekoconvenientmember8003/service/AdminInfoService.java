package neko.convenient.nekoconvenientmember8003.service;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.entity.AdminInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientmember8003.vo.AdminInfoVo;
import neko.convenient.nekoconvenientmember8003.vo.LogInVo;
import neko.convenient.nekoconvenientmember8003.vo.NewAdminInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface AdminInfoService extends IService<AdminInfo> {
    ResultObject<AdminInfoVo> logIn(LogInVo vo, HttpServletRequest request);

    void newAdmin(NewAdminInfoVo vo);

    boolean userNameIsRepeat(String userName);

    int updateUserPassword(String userName, String userPassword, String todoPassword);

    int updateUserName(String userName);

    int updateUserImagePath(String userImagePath);
}
