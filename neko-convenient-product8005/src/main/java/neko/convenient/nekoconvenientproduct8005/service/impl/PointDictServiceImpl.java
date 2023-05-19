package neko.convenient.nekoconvenientproduct8005.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientproduct8005.entity.PointDict;
import neko.convenient.nekoconvenientproduct8005.mapper.PointDictMapper;
import neko.convenient.nekoconvenientproduct8005.service.PointDictService;
import neko.convenient.nekoconvenientproduct8005.vo.NewPointDictVo;
import org.springframework.stereotype.Service;

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
                vo.getMaxPrice(),
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
        if(vo.getMaxPrice().compareTo(highestPointDict.getMaxPrice()) <= 0){
            throw new IllegalArgumentException("积分最大价格低于已有最大价格");
        }

        this.baseMapper.updateHighestPricePointDict(vo.getMaxPrice(),
                vo.getPoint(),
                LocalDateTime.now());
    }
}
