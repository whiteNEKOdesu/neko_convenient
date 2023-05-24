package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientmember8003.entity.CourierApplyInfo;
import neko.convenient.nekoconvenientmember8003.mapper.CourierApplyInfoMapper;
import neko.convenient.nekoconvenientmember8003.service.CourierApplyInfoService;
import neko.convenient.nekoconvenientmember8003.vo.ApplyCourierVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 快递员申请表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class CourierApplyInfoServiceImpl extends ServiceImpl<CourierApplyInfoMapper, CourierApplyInfo> implements CourierApplyInfoService {

    /**
     * 添加申请快递员信息
     */
    @Override
    public void applyCourier(ApplyCourierVo vo) {
        CourierApplyInfo courierApplyInfo = new CourierApplyInfo();
        BeanUtil.copyProperties(vo, courierApplyInfo);

        LocalDateTime now = LocalDateTime.now();
        courierApplyInfo.setUid(StpUtil.getLoginId().toString())
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(courierApplyInfo);
    }
}
