package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class NewPointDictVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 获得积分最高价格
     */
    @NotNull
    @Min(value = 0)
    private BigDecimal maxPrice;

    /**
     * 获得积分
     */
    @NotNull
    @Min(value = 0)
    private Integer point;
}
