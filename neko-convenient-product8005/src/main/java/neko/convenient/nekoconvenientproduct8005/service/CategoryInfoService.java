package neko.convenient.nekoconvenientproduct8005.service;

import neko.convenient.nekoconvenientproduct8005.entity.CategoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface CategoryInfoService extends IService<CategoryInfo> {
    List<CategoryInfo> getLevelCategory();
}
