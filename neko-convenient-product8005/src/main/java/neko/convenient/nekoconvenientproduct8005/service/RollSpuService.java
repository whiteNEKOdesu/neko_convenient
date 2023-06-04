package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientproduct8005.entity.RollSpu;
import neko.convenient.nekoconvenientproduct8005.vo.NewRollSpuVo;

import java.util.List;

/**
 * <p>
 * 商品轮播图表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface RollSpuService extends IService<RollSpu> {
    List<RollSpu> getRollSpuInfo();

    void newRollSpu(NewRollSpuVo vo);

    void deleteRollSpuByRollId(Integer rollId);
}
