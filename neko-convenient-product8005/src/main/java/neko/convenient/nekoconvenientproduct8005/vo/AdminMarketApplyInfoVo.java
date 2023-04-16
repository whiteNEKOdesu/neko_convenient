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
public class AdminMarketApplyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String applyId;

    /**
     * -1->正在审核，0->未通过，1->通过
     */
    @NotNull
    @Min(value = -1)
    @Max(value = 1)
    private Byte status;

    private String statusInfo;
}
