package neko.convenient.nekoconvenientorder8008.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientorder8008.entity.OrderDetailInfo;
import neko.convenient.nekoconvenientorder8008.mapper.OrderDetailInfoMapper;
import neko.convenient.nekoconvenientorder8008.service.OrderDetailInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
}
