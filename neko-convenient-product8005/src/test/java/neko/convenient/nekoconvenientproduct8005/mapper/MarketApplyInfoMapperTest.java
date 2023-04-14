package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class MarketApplyInfoMapperTest {
    @Resource
    private MarketApplyInfoMapper marketApplyInfoMapper;

    @Test
    public void updateStatusByApplyId(){
        marketApplyInfoMapper.updateStatusByApplyId("1646064098728800257",
                "1642398369596944385",
                Byte.valueOf("1"),
                "koori",
                LocalDateTime.now());
    }
}
