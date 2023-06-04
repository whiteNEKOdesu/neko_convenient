package neko.convenient.nekoconvenientorder8008.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientorder8008.entity.MQReturnMessage;

import java.util.List;

/**
 * <p>
 * 订单rabbitmq消息发送失败记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
public interface MQReturnMessageService extends IService<MQReturnMessage> {
    void deleteMQReturnMessageByReturnOrderIds(List<String> returnOrderIds);
}
