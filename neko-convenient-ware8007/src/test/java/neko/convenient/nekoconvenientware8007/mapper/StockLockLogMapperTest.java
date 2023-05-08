package neko.convenient.nekoconvenientware8007.mapper;

import neko.convenient.nekoconvenientcommonbase.utils.entity.StockStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
public class StockLockLogMapperTest {
    @Resource
    private StockLockLogMapper stockLockLogMapper;

    @Test
    public void updateStockLockLogStatus(){
        stockLockLogMapper.updateStockLockLogStatus("1655440280213635073",
                StockStatus.CANCEL_LOCK,
                LocalDateTime.now());
    }
}
