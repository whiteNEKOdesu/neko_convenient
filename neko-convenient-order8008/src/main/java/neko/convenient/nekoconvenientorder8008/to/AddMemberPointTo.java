package neko.convenient.nekoconvenientorder8008.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AddMemberPointTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uid;

    /**
     * 用户积分
     */
    private Integer point;
}
