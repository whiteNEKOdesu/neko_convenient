package neko.convenient.nekoconvenientproduct8005.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import neko.convenient.nekoconvenientproduct8005.entity.BrandInfo;
import neko.convenient.nekoconvenientproduct8005.mapper.BrandInfoMapper;
import neko.convenient.nekoconvenientproduct8005.service.BrandInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 商店信息 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class BrandInfoServiceImpl extends ServiceImpl<BrandInfoMapper, BrandInfo> implements BrandInfoService {

    /**
     * 根据品牌名查询品牌信息
     */
    @Override
    public List<BrandInfo> getBrandInfos(String brandName) {
        LambdaQueryChainWrapper<BrandInfo> brandInfoLambdaQueryChainWrapper = this.lambdaQuery();
        if(StringUtils.hasText(brandName)){
            brandInfoLambdaQueryChainWrapper.like(BrandInfo::getBrandName, brandName);
        }

        return brandInfoLambdaQueryChainWrapper.list();
    }
}
