package neko.convenient.nekoconvenientware8007.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientcommonbase.utils.entity.PreorderStatus;
import neko.convenient.nekoconvenientcommonbase.utils.entity.RabbitMqConstant;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.entity.StockStatus;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.OrderServiceException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ProductServiceException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.StockNotEnoughException;
import neko.convenient.nekoconvenientware8007.entity.StockLockLog;
import neko.convenient.nekoconvenientware8007.entity.StockUpdateLog;
import neko.convenient.nekoconvenientware8007.entity.WareInfo;
import neko.convenient.nekoconvenientware8007.feign.order.OrderLogFeignService;
import neko.convenient.nekoconvenientware8007.feign.product.SkuInfoFeignService;
import neko.convenient.nekoconvenientware8007.mapper.WareInfoMapper;
import neko.convenient.nekoconvenientware8007.service.StockLockLogService;
import neko.convenient.nekoconvenientware8007.service.StockUpdateLogService;
import neko.convenient.nekoconvenientware8007.service.WareInfoService;
import neko.convenient.nekoconvenientware8007.to.MarketInfoTo;
import neko.convenient.nekoconvenientware8007.to.OrderLogTo;
import neko.convenient.nekoconvenientware8007.vo.AddStockNumberVo;
import neko.convenient.nekoconvenientware8007.vo.LockStockVo;
import neko.convenient.nekoconvenientware8007.vo.WareInfoVo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * sku库存信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-22
 */
@Service
public class WareInfoServiceImpl extends ServiceImpl<WareInfoMapper, WareInfo> implements WareInfoService {
    @Resource
    private SkuInfoFeignService skuInfoFeignService;

    @Resource
    private OrderLogFeignService orderLogFeignService;

    @Resource
    private StockUpdateLogService stockUpdateLogService;

    @Resource
    private StockLockLogService stockLockLogService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 获取指定skuId库存信息
     */
    @Override
    public WareInfoVo getWareInfoVoBySkuId(String skuId) {
        WareInfo wareInfo = this.baseMapper.selectOne(new QueryWrapper<WareInfo>()
                .lambda()
                .eq(WareInfo::getSkuId, skuId));
        WareInfoVo vo = new WareInfoVo();
        BeanUtil.copyProperties(wareInfo, vo);
        //库存数量为 stock - lockNumber
        vo.setStock(vo.getStock() != null ? vo.getStock() - wareInfo.getLockNumber() : 0);

        return vo;
    }

    /**
     * 添加库存数量
     */
    @Override
    public void addStockNumber(AddStockNumberVo vo) {
        ResultObject<MarketInfoTo> r = skuInfoFeignService.skuIdMarketInfo(vo.getSkuId());
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }

        MarketInfoTo marketInfo = r.getResult();
        if(marketInfo == null || !StpUtil.getLoginId().equals(marketInfo.getUid())){
            throw new NotPermissionException("权限不足");
        }

        WareInfo wareInfo = this.baseMapper.selectOne(new QueryWrapper<WareInfo>()
                .lambda()
                .eq(WareInfo::getSkuId, vo.getSkuId()));

        LocalDateTime now = LocalDateTime.now();
        //库存还未初始化，新增库存
        if(wareInfo == null){
            wareInfo = new WareInfo();
            wareInfo.setMarketId(marketInfo.getMarketId())
                    .setSkuId(vo.getSkuId())
                    .setStock(vo.getAddNumber())
                    .setCreateTime(now)
                    .setUpdateTime(now);
            this.baseMapper.insert(wareInfo);
        }else{
            this.baseMapper.updateStockByWareId(wareInfo.getWareId(),
                    vo.getAddNumber(),
                    now);
        }

        StockUpdateLog stockUpdateLog = new StockUpdateLog();
        stockUpdateLog.setUid(marketInfo.getUid())
                .setUpdateNumber(vo.getAddNumber())
                .setCreateTime(now)
                .setUpdateTime(now);
        //库存更新记录
        stockUpdateLogService.newLog(stockUpdateLog);
    }

    /**
     * 锁定指定库存数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(LockStockVo vo) {
        String orderRecord = vo.getOrderRecord();
        //向延迟队列发送订单号，用于超时解锁库存
        rabbitTemplate.convertAndSend(RabbitMqConstant.STOCK_EXCHANGE_NAME,
                RabbitMqConstant.STOCK_DEAD_LETTER_ROUTING_KEY_NAME,
                orderRecord,
                new CorrelationData(orderRecord));

        List<StockLockLog> stockLockLogs = new ArrayList<>();
        List<LockStockVo.LockInfo> lockInfos = vo.getLockInfos();
        LocalDateTime now = LocalDateTime.now();

        for(LockStockVo.LockInfo lockInfo : lockInfos){
            WareInfo wareInfo = this.baseMapper.selectOne(new QueryWrapper<WareInfo>().lambda()
                    .eq(WareInfo::getSkuId, lockInfo.getSkuId()));

            if(wareInfo == null){
                throw new NoSuchResultException("无此库存");
            }

            //锁定库存
            if(this.baseMapper.lockStock(wareInfo.getWareId(), lockInfo.getLockNumber(), LocalDateTime.now()) != 1){
                throw new StockNotEnoughException("库存不足");
            }

            StockLockLog stockLockLog = new StockLockLog();
            stockLockLog.setOrderRecord(orderRecord)
                    .setWareId(wareInfo.getWareId())
                    .setLockNumber(lockInfo.getLockNumber())
                    .setCreateTime(now)
                    .setUpdateTime(now);
            //将锁定库存记录加入到 List 中
            stockLockLogs.add(stockLockLog);
        }

        //新增库存锁定记录
        stockLockLogService.saveBatch(stockLockLogs);
    }

    @Override
    public void unlockStock(String orderRecord) {
        ResultObject<OrderLogTo> r = orderLogFeignService.preorderStatus(orderRecord);
        if(!r.getResponseCode().equals(200)){
            throw new OrderServiceException("order微服务远程调用异常");
        }

        OrderLogTo result = r.getResult();
        //订单未取消，不解锁库存
        if(!result.getStatus().equals(PreorderStatus.CANCEL)){
            return;
        }

        List<StockLockLog> stockLockLogs = stockLockLogService.lambdaQuery().eq(StockLockLog::getOrderRecord, orderRecord)
                .eq(StockLockLog::getStatus, StockStatus.LOCKING)
                .list();
    }
}
