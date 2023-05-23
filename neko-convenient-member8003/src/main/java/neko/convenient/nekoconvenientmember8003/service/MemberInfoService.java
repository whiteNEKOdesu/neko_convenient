package neko.convenient.nekoconvenientmember8003.service;

import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientmember8003.entity.MemberInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientmember8003.vo.AddMemberPointVo;
import neko.convenient.nekoconvenientmember8003.vo.LogInVo;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface MemberInfoService extends IService<MemberInfo> {
    ResultObject<MemberInfoVo> logIn(LogInVo vo, HttpServletRequest request);

    int register(String userName, String userPassword, String email, String code);

    boolean userNameIsRepeat(String userName);

    void sendRegisterCode(String email);

    int updateUserPassword(String userName, String userPassword, String todoPassword);

    int updateUserName(String userName);

    int updateUserImagePath(String userImagePath);

    MemberInfoVo oAuthMemberLogIn(String sourceName,
                                  String source,
                                  String sourceUid,
                                  HttpServletRequest request);

    void addPoint(AddMemberPointVo vo);
}
