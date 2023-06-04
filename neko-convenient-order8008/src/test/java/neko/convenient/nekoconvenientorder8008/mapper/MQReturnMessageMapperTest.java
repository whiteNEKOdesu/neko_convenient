package neko.convenient.nekoconvenientorder8008.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MQReturnMessageMapperTest {
    @Resource
    private MQReturnMessageMapper mqReturnMessageMapper;

    @Test
    public void deleteMQReturnMessageByReturnOrderIds(){
        List<String> list = Arrays.asList("1655794216510500866", "1655794216510500867");
        mqReturnMessageMapper.deleteMQReturnMessageByReturnOrderIds(list, LocalDateTime.now());
    }
}
