package neko.convenient.nekoconvenientorder8008.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("receive_address")
public class ReceiveAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String receiveAddressId;

    private String uid;

    private Integer addressId;

    /**
     * 收货地址
     */
    private String addressName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
