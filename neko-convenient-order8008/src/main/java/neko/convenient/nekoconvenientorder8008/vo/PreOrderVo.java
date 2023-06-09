package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class PreOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean isFromPurchaseList = false;
}
