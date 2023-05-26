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
                Arrays.asList("1658030998299885570", "1658036323581181954", "1658037062537961474"),
                LocalDateTime.now());
    }

    @Test
    public void updateStatusToCourierConfirmByOrderId(){
        orderInfoMapper.updateStatusToCourierConfirmByOrderId("1658030998299885570", LocalDateTime.now());
    }

    @Test
    public void updateStatusToUserConfirmByOrderId(){
        orderInfoMapper.updateStatusToUserConfirmByOrderId("1658030998299885570", LocalDateTime.now());
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
