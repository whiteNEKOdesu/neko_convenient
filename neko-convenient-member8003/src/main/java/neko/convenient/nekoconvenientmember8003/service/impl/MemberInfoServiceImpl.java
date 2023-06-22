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
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Response;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.*;
import neko.convenient.nekoconvenientmember8003.entity.MemberInfo;
import neko.convenient.nekoconvenientmember8003.feign.thirdparty.MailFeignService;
import neko.convenient.nekoconvenientmember8003.mapper.MemberInfoMapper;
import neko.convenient.nekoconvenientmember8003.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientmember8003.utils.ip.IPHandler;
import neko.convenient.nekoconvenientmember8003.vo.AddMemberPointVo;
import neko.convenient.nekoconvenientmember8003.vo.LogInVo;
import neko.convenient.nekoconvenientmember8003.vo.MemberInfoVo;
import neko.convenient.nekoconvenientmember8003.vo.UpdateUserPasswordVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private MemberLevelDictService memberLevelDictService;

    @Resource
    private MailFeignService mailFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RSA rsa;

    @Override
    public ResultObject<MemberInfoVo> logIn(LogInVo vo, HttpServletRequest request) {
        ResultObject<MemberInfoVo> resultObject = new ResultObject<>();
        MemberInfo memberInfo = this.baseMapper.getMemberInfoByUserName(vo.getUserName());

        if(memberInfo == null){
            return resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
        }else{
            String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
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
    public int register(String userName, String userPassword, String email, String code) {
        if(userNameIsRepeat(userName)){
            throw new UserNameRepeatException("用户名重复");
        }

        String key = Constant.MEMBER_REDIS_PREFIX + "register_mail_code:" + email;
        String todoCode = stringRedisTemplate.opsForValue().get(key);

        if(!code.equals(todoCode)){
            throw new CodeIllegalException("验证码错误");
        }

        MemberInfo memberInfo = new MemberInfo();
        //生成盐
        String salt = Arrays.toString(RandomUtil.randomBytes(10));
        memberInfo.setUserName(userName)
                //加盐
                .setUserPassword(DigestUtils.md5DigestAsHex((userPassword + salt).getBytes()))
                .setSalt(salt)
                .setMail(email)
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
    public void sendRegisterCode(String email) {
        if(this.lambdaQuery().eq(MemberInfo::getMail, email).exists()){
            throw new EMailAlreadyExistException("邮件已经存在");
        }

        String key = Constant.MEMBER_REDIS_PREFIX + "register_mail_code:" + email;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key,
                code,
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);
        ResultObject<Object> r = mailFeignService.sendRegisterMail(email, code);

        if(r.getResponseCode() != 200){
            throw new MailSendException("邮件发送错误");
        }
    }

    @Override
    public void updateUserPassword(UpdateUserPasswordVo vo) {
        String uid = StpUtil.getLoginId().toString();
        MemberInfo memberInfo = this.baseMapper.selectById(uid);
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
        if(DigestUtils.md5DigestAsHex((userPassword + memberInfo.getSalt()).getBytes()).equals(memberInfo.getUserPassword())){
            String todoPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getTodoPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
            MemberInfo todoMemberInfo = new MemberInfo();
            todoPassword = DigestUtils.md5DigestAsHex((todoPassword + memberInfo.getSalt()).getBytes());
            todoMemberInfo.setUid(uid)
                    .setUserPassword(todoPassword)
                    .setUpdateTime(LocalDateTime.now());

            this.baseMapper.updateById(todoMemberInfo);
        }else{
            throw new LoginException("密码错误");
        }
    }

    @Override
    public void updateUserName(String userName) {
        if(!StringUtils.hasText(userName)){
            throw new IllegalArgumentException("用户名为空");
        }

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUid(StpUtil.getLoginId().toString())
                .setUserName(userName)
                .setUpdateTime(LocalDateTime.now());

        this.baseMapper.updateById(memberInfo);
    }

    @Override
    public int updateUserImagePath(String userImagePath) {
        return 0;
    }

    /**
     * 社交用户登录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberInfoVo oAuthMemberLogIn(String sourceName,
                                         String source,
                                         String sourceUid,
                                         HttpServletRequest request) {
        MemberInfo memberInfoBySourceUid = this.baseMapper.selectOne(new QueryWrapper<MemberInfo>().lambda()
            .eq(MemberInfo::getSource, source)
            .eq(MemberInfo::getSourceUid, sourceUid));
        //社交用户已注册
        if(memberInfoBySourceUid != null){
            MemberInfoVo memberInfoVo = new MemberInfoVo();
            BeanUtil.copyProperties(memberInfoBySourceUid, memberInfoVo);
            StpUtil.login(memberInfoBySourceUid.getUid());
            memberInfoVo.setToken(StpUtil.getTokenValue())
                .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(memberInfoBySourceUid.getUid()))
                .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(memberInfoBySourceUid.getUid()));
            return memberInfoVo;
        }

        //社交用户未注册开始注册
        MemberInfo memberInfo = new MemberInfo();
        LocalDateTime now = LocalDateTime.now();
        memberInfo.setUserName(sourceName + IdWorker.getTimeId())
                .setSourceName(sourceName)
                .setSource(source)
                .setSourceUid(sourceUid)
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(memberInfo);
        memberInfoBySourceUid = this.lambdaQuery().eq(MemberInfo::getSource, source)
                .eq(MemberInfo::getSourceUid, sourceUid)
                .one();

        //为用户设置普通用户角色
        userRoleRelationService.newRelation(memberInfoBySourceUid.getUid(), 1);

        MemberInfoVo memberInfoVo = new MemberInfoVo();
        BeanUtil.copyProperties(memberInfoBySourceUid, memberInfoVo);

        StpUtil.login(memberInfoBySourceUid.getUid());
        memberInfoVo.setToken(StpUtil.getTokenValue())
            .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(memberInfoBySourceUid.getUid()))
            .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(memberInfoBySourceUid.getUid()));
        memberLogInLogService.newLog(memberInfoBySourceUid.getUid(),
                IPHandler.getIP(request),
                true);

        return memberInfoVo;
    }

    /**
     * 添加用户积分
     */
    @Override
    public void addPoint(AddMemberPointVo vo) {
        MemberInfo memberInfo = this.baseMapper.selectById(vo.getUid());
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String uid = vo.getUid();
        LocalDateTime now = LocalDateTime.now();
        //添加用户积分s
        this.baseMapper.updatePointByUid(uid, vo.getPoint(), now);

        //修改用户等级
        this.baseMapper.updateLevelByUid(uid, now);
    }

    /**
     * 根据uid获取真实姓名
     */
    @Override
    public String getRealNameByUid(String uid) {
        MemberInfo memberInfo = this.baseMapper.selectById(uid);
        if(memberInfo == null || memberInfo.getRealName() == null){
            throw new NoSuchResultException("没有此用户真实姓名");
        }

        return memberInfo.getRealName();
    }
}
