package neko.convenient.nekoconvenientproduct8005.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.convenient.nekoconvenientproduct8005.entity.PointDict;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    void newPointDict(BigDecimal minPrice,
                      BigDecimal maxPrice,
                      Integer point,
                      LocalDateTime createTime,
                      LocalDateTime updateTime);

    void deleteHighestPricePointDict();

    void updateHighestPricePointDict(BigDecimal maxPrice,
                                     Integer point,
                                     LocalDateTime updateTime);

    Integer getPointByPrice(BigDecimal price);
}
