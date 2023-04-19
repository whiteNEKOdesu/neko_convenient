package neko.convenient.nekoconvenientproduct8005.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Constant;
import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.CategoryInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.CategoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientproduct8005.vo.CategoryInfoVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取层级商品分类信息
     */
    @Override
    public List<CategoryInfo> getLevelCategory() {
        String key = Constant.PRODUCT_REDIS_PREFIX + "level_category";
        String cache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(cache != null){
            return JSONUtil.toList(JSONUtil.parseArray(cache), CategoryInfo.class);
        }

        List<CategoryInfo> categoryInfos = this.list();
        //找到父分类
        List<CategoryInfo> parentCategoryInfos = categoryInfos.stream().filter(Objects::nonNull)
                .filter(categoryInfo -> categoryInfo.getLevel().equals(0))
                .collect(Collectors.toList());

        //递归设置子分类信息
        List<CategoryInfo> result = parentCategoryInfos.stream().peek(categoryInfo -> categoryInfo.setChild(getLevelCategory(categoryInfo, categoryInfos)))
                .collect(Collectors.toList());
        //缓存无数据，查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(result),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return result;
    }

    /**
     * 新增商品分类信息
     */
    @Override
    public void newCategoryInfo(CategoryInfoVo vo) {
        CategoryInfo categoryInfo = new CategoryInfo();
        BeanUtil.copyProperties(vo, categoryInfo);
        LocalDateTime now = LocalDateTime.now();
        categoryInfo.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(categoryInfo);

        String key = Constant.PRODUCT_REDIS_PREFIX + "level_category";
        //删除缓存
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除叶节点商品分类信息
     */
    @Override
    public void deleteLeafCategoryInfo(Integer categoryId) {
        this.baseMapper.deleteLeafCategoryInfo(categoryId);

        String key = Constant.PRODUCT_REDIS_PREFIX + "level_category";
        //删除缓存
        stringRedisTemplate.delete(key);
    }

    /**
     * 递归设置子分类信息
     */
    private List<CategoryInfo> getLevelCategory(CategoryInfo root, List<CategoryInfo> all){
        return all.stream().filter(categoryInfo -> root.getCategoryId().equals(categoryInfo.getParentId()))
                .peek(categoryInfo -> {
                    List<CategoryInfo> todoChild = getLevelCategory(categoryInfo, all);
                    categoryInfo.setChild(!todoChild.isEmpty() || !categoryInfo.getLevel().equals(2) ? todoChild : null);
                }).collect(Collectors.toList());
    }
}
