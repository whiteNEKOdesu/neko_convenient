package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NewOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String receiveAddressId;

    @NotBlank
    private String orderRecord;
}
