package neko.convenient.nekoconvenientmember8003.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserWeightMapperTest {
    @Resource
    private UserWeightMapper userWeightMapper;

    @Test
    public void getUserWeightByWeightType(){
        System.out.println(userWeightMapper.getUserWeightByWeightType("*"));
    }
}
