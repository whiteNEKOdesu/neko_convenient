package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientproduct8005.entity.PointDict;
import neko.convenient.nekoconvenientproduct8005.vo.NewPointDictVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 积分字典表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface PointDictService extends IService<PointDict> {
    void newPointDict(NewPointDictVo vo);

    List<PointDict> getPointDictInfos();

    void deleteHighestPricePointDict();

    void updateHighestPricePointDict(NewPointDictVo vo);

    Integer getPointByPrice(BigDecimal price);
}
