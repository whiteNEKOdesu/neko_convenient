package neko.convenient.nekoconvenientproduct8005.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import neko.convenient.nekoconvenientproduct8005.elasticsearch.entity.ProductInfoES;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProductInfoESVo implements Serializable {
    private static final long serialVersionUID = 1L;

    List<ProductInfoES> records;

    private Integer total;

    private Integer size;

    private Integer current;
}
