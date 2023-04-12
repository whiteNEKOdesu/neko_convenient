package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MarketApplyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商店所属品牌id
     */
    @NotBlank
    private String brandId;

    /**
     * 商店详细地址
     */
    @NotBlank
    private String marketAddressDescription;

    /**
     * 地址根id，对应address_dict表address_id
     */
    @NotNull
    private Integer addressId;

    /**
     * 商店许可证url
     */
    @NotBlank
    @URL
    private String certificateUrl;
}
