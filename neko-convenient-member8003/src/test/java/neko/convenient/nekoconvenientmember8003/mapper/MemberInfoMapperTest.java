package neko.convenient.nekoconvenientmember8003.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class MemberInfoMapperTest {
    @Resource
    private MemberInfoMapper memberInfoMapper;

    @Test
    public void getMemberInfoByUserName(){
        System.out.println(memberInfoMapper.getMemberInfoByUserName("NEKO"));
    }

    @Test
    public void updatePointByUid(){
        memberInfoMapper.updatePointByUid("1642067605873348610",
                5,
                LocalDateTime.now());
    }

    @Test
    public void updateLevelByUid(){
        memberInfoMapper.updateLevelByUid("1642067605873348610",
                LocalDateTime.now());
    }
}
