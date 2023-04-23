package neko.convenient.nekoconvenientware8007.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class WareInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String wareId;

    private String marketId;

    private String skuId;

    private Integer stock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
