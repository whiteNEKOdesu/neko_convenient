package neko.convenient.nekoconvenientproduct8005.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientproduct8005.entity.PointDict;
import neko.convenient.nekoconvenientproduct8005.mapper.PointDictMapper;
import neko.convenient.nekoconvenientproduct8005.service.PointDictService;
import neko.convenient.nekoconvenientproduct8005.vo.NewPointDictVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 积分字典表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class PointDictServiceImpl extends ServiceImpl<PointDictMapper, PointDict> implements PointDictService {

    /**
     * 添加积分规则
     */
    @Override
    public void newPointDict(NewPointDictVo vo) {
        PointDict highestPointDict = this.baseMapper.getHighestMaxPrice();
        if(vo.getMaxPrice().compareTo(highestPointDict.getMaxPrice()) <= 0){
            throw new IllegalArgumentException("积分最大价格低于已有最大价格");
        }

        LocalDateTime now = LocalDateTime.now();
        this.baseMapper.newPointDict(highestPointDict.getMaxPrice(),
                vo.getMaxPrice().setScale(2, BigDecimal.ROUND_DOWN),
                vo.getPoint(),
                now,
                now);
    }

    /**
     * 获取所有积分规则
     */
    @Override
    public List<PointDict> getPointDictInfos() {
        return this.lambdaQuery().orderByDesc(PointDict::getMaxPrice).list();
    }

    /**
     * 删除已有最大价格积分规则
     */
    @Override
    public void deleteHighestPricePointDict() {
        this.baseMapper.deleteHighestPricePointDict();
    }

    /**
     * 修改已有最大价格积分规则
     */
    @Override
    public void updateHighestPricePointDict(NewPointDictVo vo) {
        PointDict highestPointDict = this.baseMapper.getHighestMaxPrice();
        if(vo.getMaxPrice().compareTo(highestPointDict.getMinPrice()) <= 0){
            throw new IllegalArgumentException("积分修改最大价格低于边界");
        }

        this.baseMapper.updateHighestPricePointDict(vo.getMaxPrice().setScale(2, BigDecimal.ROUND_DOWN),
                vo.getPoint(),
                LocalDateTime.now());
    }

    /**
     * 根据价格获取积分
     */
    @Override
    public Integer getPointByPrice(BigDecimal price) {
        price = price.setScale(2, BigDecimal.ROUND_DOWN);
        Integer point = this.baseMapper.getPointByPrice(price);
        if(point == null){
            PointDict highestPointDict = this.baseMapper.getHighestMaxPrice();
            //价格超出已有最大价格积分规则范围，按已有最大价格积分规则获取积分
            if(price.compareTo(highestPointDict.getMaxPrice()) > 0){
                return highestPointDict.getPoint();
            }

            throw new NoSuchResultException("无此价格积分规则");
        }

        return point;
    }
}
