package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NewReceiveAddressVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer addressId;

    /**
     * 具体收货地址
     */
    @NotBlank
    private String addressName;
}
