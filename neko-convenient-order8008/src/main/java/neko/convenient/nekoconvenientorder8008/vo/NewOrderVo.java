package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class NewOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String receiveAddressId;

    @NotBlank
    private String orderRecord;

    @Valid
    @NotEmpty
    private List<NewOrderVo.PreOrderProductInfoVo> productInfos;

    @Data
    @Accessors(chain = true)
    public static class PreOrderProductInfoVo{
        @NotBlank
        private String skuId;

        @NotNull
        private Integer skuNumber;
    }
}
