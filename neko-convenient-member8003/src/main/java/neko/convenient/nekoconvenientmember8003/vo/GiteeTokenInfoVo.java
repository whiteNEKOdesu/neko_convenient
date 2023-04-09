package neko.convenient.nekoconvenientmember8003.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GiteeTokenInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String access_token;

    private String token_type;

    private Long expires_in;

    private String refresh_token;

    private String scope;

    private Long created_at;
}
