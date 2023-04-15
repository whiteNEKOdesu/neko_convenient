package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AddressDictMapperTest {
    @Resource
    private AddressDictMapper addressDictMapper;

    @Test
    public void getAddressInfoByAddressId(){
        System.out.println(addressDictMapper.getAddressInfoByAddressId(2682));
    }
}
