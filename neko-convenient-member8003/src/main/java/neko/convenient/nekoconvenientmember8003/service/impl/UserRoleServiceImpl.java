package neko.convenient.nekoconvenientmember8003.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientmember8003.entity.UserRole;
import neko.convenient.nekoconvenientmember8003.mapper.UserRoleMapper;
import neko.convenient.nekoconvenientmember8003.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 新增角色
     */
    @Override
    public void newUserRole(String roleType) {
        if(this.baseMapper.selectOne(new QueryWrapper<UserRole>().eq("role_type", roleType)) != null){
            throw new DuplicateKeyException("roleType重复");
        }

        UserRole userRole = new UserRole();
        LocalDateTime now = LocalDateTime.now();
        userRole.setRoleType(roleType)
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(userRole);
    }

    /**
     * 分页查询角色信息
     */
    @Override
    public Page<UserRole> getUserRolesByQueryLimitedPage(QueryVo vo) {
        Page<UserRole> page = new Page<>(vo.pageOrLimitWhenOverFlow(), vo.getLimited());
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(UserRole::getRoleType, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 查询管理员角色信息
     */
    @Override
    public List<UserRole> getAdminRoles() {
        return this.lambdaQuery().eq(UserRole::getIsAdmin, true).list();
    }
}
