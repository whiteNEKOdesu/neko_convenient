package neko.convenient.nekoconvenientmember8003.vo;

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

    private Long currentPage;

    private Long limited;
}
