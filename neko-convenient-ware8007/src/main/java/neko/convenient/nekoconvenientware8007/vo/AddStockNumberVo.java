package neko.convenient.nekoconvenientware8007.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AddStockNumberVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String skuId;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    private Integer addNumber;
}
