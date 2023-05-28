package neko.convenient.nekoconvenientmember8003.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ApplyCourierVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String idCardNumber;

    @NotBlank
    @URL
    private String idCardImage;

    @NotBlank
    private String realName;
}
