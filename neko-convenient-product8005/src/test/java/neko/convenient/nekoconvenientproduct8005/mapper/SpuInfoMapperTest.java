package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class SpuInfoMapperTest {
    @Resource
    private SpuInfoMapper spuInfoMapper;

    @Test
    public void updateSpuIsPublishBySpuId(){
        spuInfoMapper.updateSpuIsPublishBySpuId("1648241393421799425", true, LocalDateTime.now());
    }
}
