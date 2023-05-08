package neko.convenient.nekoconvenientware8007.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class WareInfoMapperTest {
    @Resource
    private WareInfoMapper wareInfoMapper;

    @Test
    public void updateStockByWareId(){
        wareInfoMapper.updateStockByWareId("1650021793701879809",
                5,
                LocalDateTime.now());
    }

    @Test
    public void lockStock(){
        wareInfoMapper.lockStock("1650021793701879809",
                5,
                LocalDateTime.now());
    }

    @Test
    public void unlockStock(){
        wareInfoMapper.unlockStock("1650021793701879809",
                "1655435506026135554",
                LocalDateTime.now());
    }
}
