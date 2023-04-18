package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SpuInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String spuName;

    @NotBlank
    private String spuDescription;

    @NotBlank
    @URL
    private String spuImage;

    /**
     * 分类id
     */
    @NotNull
    private Integer categoryId;

    /**
     * 商店id
     */
    @NotBlank
    private String marketId;
}
