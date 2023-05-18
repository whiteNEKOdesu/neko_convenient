package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class PointDictMapperTest {
    @Resource
    private PointDictMapper pointDictMapper;

    @Test
    public void getHighestMaxPrice(){
        System.out.println(pointDictMapper.getHighestMaxPrice());
    }
}
