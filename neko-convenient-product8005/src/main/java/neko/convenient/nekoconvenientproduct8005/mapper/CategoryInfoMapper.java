package neko.convenient.nekoconvenientproduct8005.mapper;

import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface CategoryInfoMapper extends BaseMapper<CategoryInfo> {
    void deleteLeafCategoryInfo(Integer categoryId);
}
