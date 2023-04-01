package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientmember8003.entity.WeightRoleRelation;
import neko.convenient.nekoconvenientmember8003.mapper.WeightRoleRelationMapper;
import neko.convenient.nekoconvenientmember8003.service.UserRoleRelationService;
import neko.convenient.nekoconvenientmember8003.service.WeightRoleRelationService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 权限，角色关系表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class WeightRoleRelationServiceImpl extends ServiceImpl<WeightRoleRelationMapper, WeightRoleRelation> implements WeightRoleRelationService {
    @Resource
    private UserRoleRelationService userRoleRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户权限信息获取
     */
    @Override
    public List<WeightRoleRelation> getRelations(String uid) {
        String key = Constant.REDIS_PREFIX + StpUtil.getLoginId();
        System.out.println(key);
        String relationCache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(relationCache != null){
            return JSONUtil.toList(JSONUtil.parseArray(relationCache), WeightRoleRelation.class);
        }

        List<WeightRoleRelation> relations = this.baseMapper.getRelationsByRoleIds(userRoleRelationService.getUserRoleIds(uid));
        //缓存无数据，查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(relations),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return relations;
    }
}
