package neko.convenient.nekoconvenientorder8008.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
public class OrderInfoMapperTest {
    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Test
    public void getUnpickOrderInfoByQueryLimitedPage(){
        System.out.println(orderInfoMapper.getUnpickOrderInfoByQueryLimitedPage(8,
                0,
                null));
    }

    @Test
    public void getUnpickOrderInfoByQueryLimitedPageNumber(){
        System.out.println(orderInfoMapper.getUnpickOrderInfoByQueryLimitedPageNumber(null));
    }

    @Test
    public void updateCourierIdByOrderIds(){
        orderInfoMapper.updateCourierIdByOrderIds("1642067605873348610",
                Arrays.asList("202305151645490681658030913113571330", "202305151706328041658036129724645377", "202305151709509231658036960696066049"),
                LocalDateTime.now());
    }

    @Test
    public void updateStatusToCourierConfirmByOrderId(){
        orderInfoMapper.updateStatusToCourierConfirmByOrderId("202305151645490681658030913113571330", LocalDateTime.now());
    }

    @Test
    public void updateStatusToUserConfirmByOrderId(){
        orderInfoMapper.updateStatusToUserConfirmByOrderId("202305151645490681658030913113571330", LocalDateTime.now());
    }

    @Test
    public void getUserSelfPickOrderInfoByQueryLimitedPage(){
        System.out.println(orderInfoMapper.getUserSelfPickOrderInfoByQueryLimitedPage(8,
                0,
                null,
                "1642067605873348610"));
    }

    @Test
    public void getUserSelfPickOrderInfoByQueryLimitedPageNumber(){
        System.out.println(orderInfoMapper.getUserSelfPickOrderInfoByQueryLimitedPageNumber(null, "1642067605873348610"));
    }
}
