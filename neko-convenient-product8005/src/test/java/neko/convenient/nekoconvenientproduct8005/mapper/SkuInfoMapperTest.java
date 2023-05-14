package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
public class SkuInfoMapperTest {
    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Test
    public void getSkuInfoVosBySpuId(){
        System.out.println(skuInfoMapper.getSkuInfoVosBySpuId("1650745629774811137"));
    }

    @Test
    public void getProductInfosBySkuIds(){
        System.out.println(skuInfoMapper.getProductInfosBySkuIds(Arrays.asList("1648589292781449218", "1648603469965189121")));
    }

    @Test
    public void getOrderDetailInfoBySkuId(){
        System.out.println(skuInfoMapper.getOrderDetailInfoBySkuId("1648589292781449218"));
    }
}
