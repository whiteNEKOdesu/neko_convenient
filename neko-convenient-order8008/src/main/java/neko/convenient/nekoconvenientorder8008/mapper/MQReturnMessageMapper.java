package neko.convenient.nekoconvenientorder8008.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.convenient.nekoconvenientorder8008.entity.MQReturnMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单rabbitmq消息发送失败记录表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Mapper
public interface MQReturnMessageMapper extends BaseMapper<MQReturnMessage> {

}
