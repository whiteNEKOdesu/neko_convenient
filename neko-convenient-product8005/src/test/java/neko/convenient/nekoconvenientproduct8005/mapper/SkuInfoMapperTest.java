package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class SkuInfoMapperTest {
    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Test
    public void getSkuInfoVosBySpuId(){
        System.out.println(skuInfoMapper.getSkuInfoVosBySpuId("1650745629774811137"));
    }
}
