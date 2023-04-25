package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProductInfoESQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String brandId;

    private String marketId;

    private Integer categoryId;

    private Integer addressId;

    @Min(value = 0)
    private BigDecimal minPrice;

    @Min(value = 0)
    private BigDecimal maxPrice;

    private String queryWords;

    @NotNull
    @Min(value = 0)
    @Max(value = 50)
    private Integer currentPage;

    @NotNull
    @Min(value = 5)
    @Max(value = 50)
    private Integer limited;
}
