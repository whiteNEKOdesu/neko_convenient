package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ApplyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 申请注册品牌名
     */
    @NotBlank
    private String brandName;

    /**
     * 申请注册品牌描述
     */
    @NotBlank
    private String brandDescription;

    /**
     * 公司证书url
     */
    @NotBlank
    @URL
    private String certificateUrl;
}
