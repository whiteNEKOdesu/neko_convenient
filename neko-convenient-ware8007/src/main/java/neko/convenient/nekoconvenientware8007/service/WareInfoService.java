package neko.convenient.nekoconvenientware8007.service;

import neko.convenient.nekoconvenientware8007.entity.WareInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientware8007.vo.AddStockNumberVo;
import neko.convenient.nekoconvenientware8007.vo.LockStockVo;
import neko.convenient.nekoconvenientware8007.vo.WareInfoVo;

/**
 * <p>
 * sku库存信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
public interface WareInfoService extends IService<WareInfo> {
    WareInfoVo getWareInfoVoBySkuId(String skuId);

    void addStockNumber(AddStockNumberVo vo);

    void lockStock(LockStockVo vo);

    void unlockStock(String orderRecord);
}
