package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.MemberServiceException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.OrderPickException;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;
import neko.convenient.nekoconvenientorder8008.feign.member.MemberInfoFeignService;
import neko.convenient.nekoconvenientorder8008.mapper.OrderDetailInfoMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderDetailInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-05-02
 */
@Service
public class OrderDetailInfoServiceImpl extends ServiceImpl<OrderDetailInfoMapper, OrderDetailInfo> implements OrderDetailInfoService {
    @Resource
    private MemberInfoFeignService memberInfoFeignService;

    /**
     * 分页查询用户自身购买商品信息
     */
    @Override
    public Page<OrderDetailInfo> getUserSelfOrderDetailInfoByQueryLimitedPage(QueryVo vo) {
        Page<OrderDetailInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<OrderDetailInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetailInfo::getUid, StpUtil.getLoginId().toString())
                .orderByDesc(OrderDetailInfo::getOrderDetailId);
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(OrderDetailInfo::getOrderRecord, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 将订单详情状态修改为完成
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userConfirmDelivered(String orderRecord) {
        this.baseMapper.updateStatusToDeliveredByOrderRecord(orderRecord, LocalDateTime.now());
    }

    /**
     * 修改订单详情快递员信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourierInfoByOrderRecords(List<String> orderRecords) {
        String uid = StpUtil.getLoginId().toString();
        ResultObject<String> r = memberInfoFeignService.realNameInfo(uid);
        //远程调用member微服务获取真实姓名
        if(!r.getResponseCode().equals(200)){
            throw new MemberServiceException("member微服务远程调用异常");
        }

        if(this.baseMapper.updateCourierInfoByOrderRecords(orderRecords,
                uid,
                r.getResult(),
                LocalDateTime.now()) != orderRecords.size()){
            throw new OrderPickException("快递员接单错误");
        }
    }
}
