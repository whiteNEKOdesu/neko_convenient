package neko.convenient.nekoconvenientorder8008.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
public class OrderDetailInfoMapperTest {
    @Resource
    private OrderDetailInfoMapper orderDetailInfoMapper;

    @Test
    public void updateStatusToDeliveredByOrderRecord(){
        orderDetailInfoMapper.updateStatusToDeliveredByOrderRecord("202305151645490681658030913113571330", LocalDateTime.now());
    }

    @Test
    public void updateCourierInfoByOrderRecords(){
        orderDetailInfoMapper.updateCourierInfoByOrderRecords(Arrays.asList("202305151645490681658030913113571330", "202305151706328041658036129724645377", "202305151709509231658036960696066049"),
                "1642067605873348610",
                "NEKO",
                LocalDateTime.now());
    }
}
