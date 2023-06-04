package neko.convenient.nekoconvenientorder8008.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientorder8008.entity.MQReturnMessage;
import neko.convenient.nekoconvenientorder8008.mapper.MQReturnMessageMapper;
import neko.convenient.nekoconvenientorder8008.service.MQReturnMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单rabbitmq消息发送失败记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Service
public class MQReturnMessageServiceImpl extends ServiceImpl<MQReturnMessageMapper, MQReturnMessage> implements MQReturnMessageService {

    /**
     * 根据returnOrderId list删除订单rabbitmq消息发送失败记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMQReturnMessageByReturnOrderIds(List<String> returnOrderIds) {
        this.baseMapper.deleteMQReturnMessageByReturnOrderIds(returnOrderIds, LocalDateTime.now());
    }
}
