package neko.convenient.nekoconvenientorder8008.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class OrderLogMapperTest {
    @Resource
    private OrderLogMapper orderLogMapper;

    @Test
    public void updateOrderLogStatusToCancel(){
        orderLogMapper.updateOrderLogStatusToCancel("202305081344025031655448452588445697",
                LocalDateTime.now());
    }
}
