package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientproduct8005.entity.RollSpu;
import neko.convenient.nekoconvenientproduct8005.mapper.RollSpuMapper;
import neko.convenient.nekoconvenientproduct8005.service.RollSpuService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品轮播图表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class RollSpuServiceImpl extends ServiceImpl<RollSpuMapper, RollSpu> implements RollSpuService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取商品轮播图信息
     */
    @Override
    public List<RollSpu> getRollSpuInfo() {
        String key = Constant.PRODUCT_REDIS_PREFIX + "roll_spu";
        String cache = stringRedisTemplate.opsForValue().get(key);
        //缓存有数据
        if(cache != null){
            return JSONUtil.toList(JSONUtil.parseArray(cache), RollSpu.class);
        }

        List<RollSpu> result = this.lambdaQuery().orderBy(true, true, RollSpu::getSort).list();
        //缓存无数据，查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(result),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return result;
    }
}
