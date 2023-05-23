package neko.convenient.nekoconvenientmember8003.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AddMemberPointVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String uid;

    /**
     * 用户积分
     */
    @NotNull
    @Min(value = 1)
    private Integer point;
}
