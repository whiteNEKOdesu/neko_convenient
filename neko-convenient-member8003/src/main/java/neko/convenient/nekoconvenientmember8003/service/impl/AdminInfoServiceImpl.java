package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Response;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.UserNameRepeatException;
import neko.convenient.nekoconvenientmember8003.entity.AdminInfo;
import neko.convenient.nekoconvenientmember8003.mapper.AdminInfoMapper;
import neko.convenient.nekoconvenientmember8003.service.AdminInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientmember8003.service.AdminLogInLogService;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import neko.convenient.nekoconvenientmember8003.service.WeightRoleRelationService;
import neko.convenient.nekoconvenientmember8003.utils.ip.IPHandler;
import neko.convenient.nekoconvenientmember8003.vo.AdminInfoVo;
import neko.convenient.nekoconvenientmember8003.vo.LogInVo;
import neko.convenient.nekoconvenientmember8003.vo.NewAdminInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {
    @Resource
    private UserRoleRelationService userRoleRelationService;

    @Resource
    private AdminLogInLogService adminLogInLogService;

    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Resource
    private RSA rsa;

    @Override
    public ResultObject<AdminInfoVo> logIn(LogInVo vo, HttpServletRequest request) {
        ResultObject<AdminInfoVo> resultObject = new ResultObject<>();
        AdminInfo adminInfo = this.baseMapper.getAdminInfoByUserName(vo.getUserName());

        if(adminInfo == null){
            return resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
        }else{
            String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
            if(DigestUtils.md5DigestAsHex((userPassword + adminInfo.getSalt()).getBytes()).equals(adminInfo.getUserPassword())){
                StpUtil.login(adminInfo.getAdminId());
                AdminInfoVo adminInfoVo = new AdminInfoVo();
                BeanUtil.copyProperties(adminInfo, adminInfoVo);
                adminInfoVo.setToken(StpUtil.getTokenValue())
                    .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(adminInfo.getAdminId()))
                    .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(adminInfo.getAdminId()));
                resultObject.setResult(adminInfoVo)
                        .setResponseStatus(Response.SUCCESS);
                adminLogInLogService.newLog(adminInfo.getAdminId(),
                        IPHandler.getIP(request),
                        true);
            }else{
                resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
                adminLogInLogService.newLog(adminInfo.getAdminId(),
                        IPHandler.getIP(request),
                        false);
            }
        }

        return resultObject.compact();
    }

    /**
     * 新增管理员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newAdmin(NewAdminInfoVo vo) {
        if(userNameIsRepeat(vo.getUserName())){
            throw new UserNameRepeatException("用户名重复");
        }

        AdminInfo adminInfo = new AdminInfo();
        //生成盐
        String salt = Arrays.toString(RandomUtil.randomBytes(10));
        BeanUtil.copyProperties(vo, adminInfo);
        adminInfo.setSalt(salt)
            .setOperateAdminId(StpUtil.getLoginId().toString())
            .setCreateTime(LocalDateTime.now())
            .setUpdateTime(LocalDateTime.now());

        this.baseMapper.insert(adminInfo);
        AdminInfo adminInfoByUserName = this.baseMapper.getAdminInfoByUserName(vo.getUserName());

        //设置管理员角色
        userRoleRelationService.newRelations(adminInfoByUserName.getAdminId(), vo.getRoleIds());
    }

    @Override
    public boolean userNameIsRepeat(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<AdminInfo>().eq("user_name", userName)) != null;
    }

    @Override
    public int updateUserPassword(String userName, String userPassword, String todoPassword) {
        return 0;
    }

    @Override
    public int updateUserName(String userName) {
        return 0;
    }

    @Override
    public int updateUserImagePath(String userImagePath) {
        return 0;
    }
}
