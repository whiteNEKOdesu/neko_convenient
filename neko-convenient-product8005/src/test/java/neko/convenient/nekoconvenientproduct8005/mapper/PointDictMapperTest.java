package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
public class PointDictMapperTest {
    @Resource
    private PointDictMapper pointDictMapper;

    @Test
    public void getHighestMaxPrice(){
        System.out.println(pointDictMapper.getHighestMaxPrice());
    }

    @Test
    public void newPointDict(){
        pointDictMapper.newPointDict(new BigDecimal("1000"),
                new BigDecimal("5000"),
                50,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    public void deleteHighestPricePointDict(){
        pointDictMapper.deleteHighestPricePointDict();
    }

    @Test
    public void updateHighestPricePointDict(){
        pointDictMapper.updateHighestPricePointDict(new BigDecimal("1500"),
                15,
                LocalDateTime.now());
    }
}
