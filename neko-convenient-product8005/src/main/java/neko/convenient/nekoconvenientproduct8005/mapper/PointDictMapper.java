package neko.convenient.nekoconvenientproduct8005.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.convenient.nekoconvenientproduct8005.entity.PointDict;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 积分字典表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface PointDictMapper extends BaseMapper<PointDict> {
    PointDict getHighestMaxPrice();
}
