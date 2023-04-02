package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Response;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.UserNameRepeatException;
import neko.convenient.nekoconvenientmember8003.entity.MemberInfo;
import neko.convenient.nekoconvenientmember8003.mapper.MemberInfoMapper;
import neko.convenient.nekoconvenientmember8003.service.MemberInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientmember8003.service.MemberLogInLogService;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import neko.convenient.nekoconvenientmember8003.service.WeightRoleRelationService;
import neko.convenient.nekoconvenientmember8003.utils.ip.IPHandler;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo> implements MemberInfoService {
    @Resource
    private UserRoleRelationService userRoleRelationService;

    @Resource
    private MemberLogInLogService memberLogInLogService;

    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Override
    public ResultObject<MemberInfoVo> logIn(String userName, String userPassword, HttpServletRequest request) {
        ResultObject<MemberInfoVo> resultObject = new ResultObject<>();
        MemberInfo memberInfo = this.baseMapper.getMemberInfoByUserName(userName);

        if(memberInfo == null){
            return resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
        }else{
            if(DigestUtils.md5DigestAsHex((userPassword + memberInfo.getSalt()).getBytes()).equals(memberInfo.getUserPassword())){
                StpUtil.login(memberInfo.getUid());
                MemberInfoVo memberInfoVo = new MemberInfoVo();
                BeanUtil.copyProperties(memberInfo, memberInfoVo);
                memberInfoVo.setToken(StpUtil.getTokenValue())
                    .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(memberInfo.getUid()))
                    .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(memberInfo.getUid()));
                resultObject.setResult(memberInfoVo)
                        .setResponseStatus(Response.SUCCESS);
                memberLogInLogService.newLog(memberInfo.getUid(),
                        IPHandler.getIP(request),
                        true);
            }else{
                resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
                memberLogInLogService.newLog(memberInfo.getUid(),
                        IPHandler.getIP(request),
                        false);
            }
        }
        return resultObject.compact();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int register(String userName, String userPassword) {
        if(userNameIsRepeat(userName)){
            throw new UserNameRepeatException("用户名重复");
        }

        MemberInfo memberInfo = new MemberInfo();
        //生成盐
        String salt = Arrays.toString(RandomUtil.randomBytes(10));
        memberInfo.setUserName(userName)
                //加盐
                .setUserPassword(DigestUtils.md5DigestAsHex((userPassword + salt).getBytes()))
                .setSalt(salt)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        this.baseMapper.insert(memberInfo);
        MemberInfo memberInfoByUserName = this.baseMapper.getMemberInfoByUserName(userName);

        //为用户设置普通用户角色
        return userRoleRelationService.newRelation(memberInfoByUserName.getUid(), 1);
    }

    @Override
    public boolean userNameIsRepeat(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<MemberInfo>().eq("user_name", userName)) != null;
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
