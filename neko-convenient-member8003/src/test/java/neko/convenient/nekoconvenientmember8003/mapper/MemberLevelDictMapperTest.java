package neko.convenient.nekoconvenientmember8003.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MemberLevelDictMapperTest {
    @Resource
    private MemberLevelDictMapper memberLevelDictMapper;

    @Test
    public void getHighestLevel(){
        System.out.println(memberLevelDictMapper.getHighestLevel());
    }

    @Test
    public void getSecondLevel(){
        System.out.println(memberLevelDictMapper.getSecondLevel());
    }

    @Test
    public void getMemberLevelDictByUid(){
        System.out.println(memberLevelDictMapper.getMemberLevelDictByUid("1642067605873348610"));
    }
}
