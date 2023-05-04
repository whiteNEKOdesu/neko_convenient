package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String orderRecord;

    @NotBlank
    private String receiveAddressId;

    @NotEmpty
    private List<OrderProductInfo> productInfos;

    @Data
    @Accessors(chain = true)
    public static class OrderProductInfo{
        private String skuId;

        private Integer skuNumber;
    }
}
