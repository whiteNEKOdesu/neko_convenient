package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class SkuInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skuId;

    @NotBlank
    private String spuId;

    @NotBlank
    private String skuName;

    @NotBlank
    @URL
    private String skuImage;

    @NotNull
    private BigDecimal price;
}
