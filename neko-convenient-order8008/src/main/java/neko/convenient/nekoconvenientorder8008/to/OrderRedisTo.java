package neko.convenient.nekoconvenientorder8008.to;

import lombok.Data;
import lombok.experimental.Accessors;
import neko.convenient.nekoconvenientorder8008.vo.ProductInfoVo;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderRedisTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String receiveAddressId;

    private String orderRecord;

    private List<ProductInfoVo> productInfos;
}
