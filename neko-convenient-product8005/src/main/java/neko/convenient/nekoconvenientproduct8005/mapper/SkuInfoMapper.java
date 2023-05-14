package neko.convenient.nekoconvenientproduct8005.mapper;

import neko.convenient.nekoconvenientproduct8005.entity.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.convenient.nekoconvenientproduct8005.vo.OrderDetailInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.ProductInfoVo;
import neko.convenient.nekoconvenientproduct8005.vo.SkuInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * sku信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {
    List<SkuInfoVo> getSkuInfoVosBySpuId(String spuId);

    List<ProductInfoVo> getProductInfosBySkuIds(List<String> skuIds);

    OrderDetailInfoVo getOrderDetailInfoBySkuId(String skuId);
}
