package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RoleType;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientmember8003.entity.CourierApplyInfo;
import neko.convenient.nekoconvenientmember8003.entity.UserRole;
import neko.convenient.nekoconvenientmember8003.mapper.CourierApplyInfoMapper;
import neko.convenient.nekoconvenientmember8003.service.CourierApplyInfoService;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import neko.convenient.nekoconvenientmember8003.service.UserRoleService;
import neko.convenient.nekoconvenientmember8003.vo.AdminHandleCourierApplyVo;
import neko.convenient.nekoconvenientmember8003.vo.ApplyCourierVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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

        //通过申请
        if(vo.getStatus().equals(Byte.valueOf("1"))){
            UserRole userRole = userRoleService.getUserRoleByRoleType(RoleType.COURIER);
            //为用户添加快递员角色
            userRoleRelationService.newRelation(courierApplyInfo.getUid(), userRole.getRoleId());

            String key = Constant.MEMBER_REDIS_PREFIX + "weight_cache:" + courierApplyInfo.getUid();
            //删除缓存
            stringRedisTemplate.delete(key);
        }

        CourierApplyInfo todoUpdateApplyInfo = new CourierApplyInfo();
        todoUpdateApplyInfo.setApplyId(courierApplyInfo.getApplyId())
                .setApplyAdminId(StpUtil.getLoginId().toString())
                .setStatus(vo.getStatus())
                .setStatusInfo(vo.getStatusInfo())
                .setUpdateTime(LocalDateTime.now());
        //修改申请状态
        this.baseMapper.updateById(todoUpdateApplyInfo);
    }
}
