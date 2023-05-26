package neko.convenient.nekoconvenientorder8008.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class OrderDetailInfoMapperTest {
    @Resource
    private OrderDetailInfoMapper orderDetailInfoMapper;

    @Test
    public void updateStatusToDeliveredByOrderRecord(){
        orderDetailInfoMapper.updateStatusToDeliveredByOrderRecord("202305151645490681658030913113571330", LocalDateTime.now());
    }
}
