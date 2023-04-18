package neko.convenient.nekoconvenientcommonbase.utils.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class QueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long uid;

    private Object objectId;

    private String queryWords;

    @NotNull
    @Min(0)
    @Max(50)
    private Integer currentPage;

    @NotNull
    @Min(5)
    @Max(50)
    private Integer limited;

    public Integer pageOrLimitWhenOverFlow(){
        return currentPage <= 50 ? currentPage : 50;
    }
}
