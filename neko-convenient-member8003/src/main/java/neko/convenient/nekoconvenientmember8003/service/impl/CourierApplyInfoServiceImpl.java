package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientmember8003.entity.CourierApplyInfo;
import neko.convenient.nekoconvenientmember8003.entity.MemberInfo;
import neko.convenient.nekoconvenientmember8003.entity.UserRole;
import neko.convenient.nekoconvenientmember8003.mapper.CourierApplyInfoMapper;
import neko.convenient.nekoconvenientmember8003.service.*;
import neko.convenient.nekoconvenientmember8003.vo.AdminHandleCourierApplyVo;
import neko.convenient.nekoconvenientmember8003.vo.ApplyCourierVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 快递员申请表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class CourierApplyInfoServiceImpl extends ServiceImpl<CourierApplyInfoMapper, CourierApplyInfo> implements CourierApplyInfoService {
    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserRoleRelationService userRoleRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Resource
    private MemberInfoService memberInfoService;

    /**
     * 添加申请快递员信息
     */
    @Override
    public void applyCourier(ApplyCourierVo vo) {
        CourierApplyInfo courierApplyInfo = new CourierApplyInfo();
        BeanUtil.copyProperties(vo, courierApplyInfo);

        LocalDateTime now = LocalDateTime.now();
        courierApplyInfo.setUid(StpUtil.getLoginId().toString())
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(courierApplyInfo);
    }

    /**
     * 管理员处理快递员申请信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleApply(AdminHandleCourierApplyVo vo) {
        CourierApplyInfo courierApplyInfo = this.baseMapper.selectById(vo.getApplyId());
        if(courierApplyInfo == null || !courierApplyInfo.getStatus().equals(Byte.valueOf("-1"))){
            throw new NoSuchResultException("没有此快递员申请请求");
        }

        List<String> roleTypes = weightRoleRelationService.getRoleTypesByUid(courierApplyInfo.getUid());
        for(String role : roleTypes){
            //已经拥有 courier 角色
            if(RoleType.COURIER.equals(role)){
                return;
            }
        }

        LocalDateTime now = LocalDateTime.now();
        //通过申请
        if(vo.getStatus().equals(Byte.valueOf("1"))){
            UserRole userRole = userRoleService.getUserRoleByRoleType(RoleType.COURIER);
            //为用户添加快递员角色
            userRoleRelationService.newRelation(courierApplyInfo.getUid(), userRole.getRoleId());

            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setUid(courierApplyInfo.getUid())
                    .setIdCardNumber(courierApplyInfo.getIdCardNumber())
                    .setIdCardImage(courierApplyInfo.getIdCardImage())
                    .setUpdateTime(now);
            //为用户设置身份证号
            memberInfoService.updateById(memberInfo);

            String key = Constant.MEMBER_REDIS_PREFIX + "weight_cache:" + courierApplyInfo.getUid();
            //删除缓存
            stringRedisTemplate.delete(key);
        }

        CourierApplyInfo todoUpdateApplyInfo = new CourierApplyInfo();
        todoUpdateApplyInfo.setApplyId(courierApplyInfo.getApplyId())
                .setApplyAdminId(StpUtil.getLoginId().toString())
                .setStatus(vo.getStatus())
                .setStatusInfo(vo.getStatusInfo())
                .setUpdateTime(now);
        //修改申请状态
        this.baseMapper.updateById(todoUpdateApplyInfo);
    }

    /**
     * 分页查询未处理快递员申请信息
     */
    @Override
    public Page<CourierApplyInfo> getCourierApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<CourierApplyInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<CourierApplyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CourierApplyInfo::getStatus, Byte.valueOf("-1"));
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(CourierApplyInfo::getUid, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 分页查询用户自身快递员申请信息
     */
    @Override
    public Page<CourierApplyInfo> getUserSelfCourierApplyInfoByQueryLimitedPage(QueryVo vo) {
        Page<CourierApplyInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<CourierApplyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CourierApplyInfo::getUid, StpUtil.getLoginId().toString())
                .orderByDesc(CourierApplyInfo::getApplyId);
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(CourierApplyInfo::getApplyId, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
