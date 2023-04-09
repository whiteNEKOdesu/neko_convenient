package neko.convenient.nekoconvenientmember8003.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GiteeMemberVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String login;

    private String name;

    private String avatar_url;

    private String url;

    private String html_url;

    private String type;

    private String created_at;

    private String updated_at;

    private String email;

    private TokenInfo tokenInfo;
}
