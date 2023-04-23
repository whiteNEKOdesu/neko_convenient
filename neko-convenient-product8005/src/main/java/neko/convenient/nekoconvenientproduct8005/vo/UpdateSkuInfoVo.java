package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class UpdateSkuInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String skuId;

    private String skuName;

    @URL
    private String skuImage;

    private BigDecimal price;
}
