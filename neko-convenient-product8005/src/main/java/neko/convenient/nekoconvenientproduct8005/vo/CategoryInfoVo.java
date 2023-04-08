package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CategoryInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer parentId;

    /**
     * 分类层级，0，1，2,最大为2
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer level;

    /**
     * 分类名
     */
    @NotBlank
    private String categoryName;
}
