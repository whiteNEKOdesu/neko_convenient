package neko.convenient.nekoconvenientorder8008.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class CourierPickOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private List<String> orderRecords;
}
