package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.OutOfLimitationException;
import neko.convenient.nekoconvenientproduct8005.entity.RollSpu;
import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.RollSpuMapper;
import neko.convenient.nekoconvenientproduct8005.service.RollSpuService;
import neko.convenient.nekoconvenientproduct8005.service.SpuInfoService;
import neko.convenient.nekoconvenientproduct8005.vo.NewRollSpuVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    private SpuInfoService spuInfoService;

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

    /**
     * 管理员添加商品轮播图信息
     */
    @Override
    public void newRollSpu(NewRollSpuVo vo) {
        List<RollSpu> list = this.list();
        if(list.size() > 9){
            throw new OutOfLimitationException("轮播图数量超出限制");
        }

        SpuInfo spuInfo = spuInfoService.getSpuInfoBySpuId(vo.getSpuId());
        if(spuInfo == null){
            throw new NoSuchResultException("无此spuId spu信息");
        }

        RollSpu rollSpu = new RollSpu();
        BeanUtil.copyProperties(vo, rollSpu);
        LocalDateTime now = LocalDateTime.now();
        rollSpu.setCreateTime(now)
                .setUpdateTime(now);
        //vo中轮播图片为空，则使用spu图片
        if(vo.getSpuImage() == null){
            rollSpu.setSpuImage(spuInfo.getSpuImage());
        }

        this.baseMapper.insert(rollSpu);

        String key = Constant.PRODUCT_REDIS_PREFIX + "roll_spu";
        //删除缓存
        stringRedisTemplate.delete(key);
    }

    /**
     * 管理员根据rollId删除商品轮播图信息
     */
    @Override
    public void deleteRollSpuByRollId(Integer rollId) {
        this.baseMapper.deleteById(rollId);
    }
}
