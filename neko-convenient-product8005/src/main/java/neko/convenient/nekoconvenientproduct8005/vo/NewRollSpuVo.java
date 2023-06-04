package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NewRollSpuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 轮播图标题
     */
    @NotBlank
    private String rollName;

    @NotBlank
    private String spuId;

    @URL
    private String spuImage;

    /**
     * 排序字段，越小越靠前
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 8)
    private Integer sort;
}
