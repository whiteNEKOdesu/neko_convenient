package neko.convenient.nekoconvenientcommonbase.utils.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class QueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long uid;

    private Object objectId;

    private String queryWords;

    private Integer currentPage;

    private Integer limited;

    public Integer pageOrLimitWhenOverFlow(){
        return currentPage <= 50 ? currentPage : 50;
    }
}
