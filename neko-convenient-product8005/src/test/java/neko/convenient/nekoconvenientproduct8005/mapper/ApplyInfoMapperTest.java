package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class ApplyInfoMapperTest {
    @Resource
    private ApplyInfoMapper applyInfoMapper;

    @Test
    public void updateStatusByApplyId(){
        applyInfoMapper.updateStatusByApplyId("1646064211975008258",
                "1642398369596944385",
                Byte.valueOf("1"),
                "koori",
                LocalDateTime.now());
    }
}
