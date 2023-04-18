package neko.convenient.nekoconvenientproduct8005.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CategoryInfoMapperTest {
    @Resource
    private CategoryInfoMapper categoryInfoMapper;

    @Test
    public void deleteLeafCategoryInfo(){
        categoryInfoMapper.deleteLeafCategoryInfo(1);
    }

    @Test
    public void getFullCategoryName(){
        System.out.println(categoryInfoMapper.getFullCategoryName(9));
    }
}
