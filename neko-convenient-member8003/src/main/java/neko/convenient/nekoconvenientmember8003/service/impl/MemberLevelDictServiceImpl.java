package neko.convenient.nekoconvenientmember8003.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientcommonbase.utils.exception.NoSuchResultException;
import neko.convenient.nekoconvenientmember8003.entity.MemberLevelDict;
import neko.convenient.nekoconvenientmember8003.mapper.MemberLevelDictMapper;
import neko.convenient.nekoconvenientmember8003.service.MemberLevelDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.convenient.nekoconvenientmember8003.vo.NewMemberLevelVo;
import neko.convenient.nekoconvenientmember8003.vo.UpdateMemberLevelVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户等级字典表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Service
public class MemberLevelDictServiceImpl extends ServiceImpl<MemberLevelDictMapper, MemberLevelDict> implements MemberLevelDictService {

    /**
     * 新增用户等级
     */
    @Override
    public void newMemberLevel(NewMemberLevelVo vo) {
        MemberLevelDict highestLevel = this.baseMapper.getHighestLevel();
        if(!highestLevel.getMemberLevel().equals(vo.getMemberLevel() - 1) ||
                vo.getAchievePoint() <= highestLevel.getAchievePoint()){
            throw new IllegalArgumentException("等级信息设置非法");
        }
        MemberLevelDict memberLevelDict = new MemberLevelDict();
        BeanUtil.copyProperties(vo, memberLevelDict);
        LocalDateTime now = LocalDateTime.now();
        memberLevelDict.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(memberLevelDict);
    }

    /**
     * 分页查询用户等级信息
     */
    @Override
    public Page<MemberLevelDict> getMemberLevelDictByQueryLimitedPage(QueryVo vo) {
        Page<MemberLevelDict> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<MemberLevelDict> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(MemberLevelDict::getLevelName, vo.getQueryWords());
        }
        queryWrapper.lambda().orderByDesc(MemberLevelDict::getMemberLevel);

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 修改用户等级信息
     */
    @Override
    public void updateMemberLevelDict(UpdateMemberLevelVo vo) {
        if(vo.isEmpty()){
            throw new IllegalArgumentException("等级修改内容不能全为空");
        }
        MemberLevelDict memberLevelInfo = this.baseMapper.selectById(vo.getMemberLevelId());
        MemberLevelDict lowerLevel = this.baseMapper.selectOne(new QueryWrapper<MemberLevelDict>().lambda()
                .eq(MemberLevelDict::getMemberLevel, memberLevelInfo.getMemberLevel() - 1));
        if(vo.getAchievePoint() <= lowerLevel.getAchievePoint()){
            throw new IllegalArgumentException("等级信息设置非法");
        }

        MemberLevelDict memberLevelDict = new MemberLevelDict();
        BeanUtil.copyProperties(vo, memberLevelDict);
        memberLevelDict.setUpdateTime(LocalDateTime.now());

        this.baseMapper.updateById(memberLevelDict);
    }

    /**
     * 根据uid获取用户等级信息
     */
    @Override
    public MemberLevelDict getMemberLevelDictByUid(String uid) {
        MemberLevelDict memberLevelDict = this.baseMapper.getMemberLevelDictByUid(uid);
        if(memberLevelDict == null){
            throw new NoSuchResultException("无此用户等级信息");
        }

        return memberLevelDict;
    }

    /**
     * 根据积分获取用户等级
     */
    @Override
    public Integer getMemberLevelByPoint(Integer point) {
        Integer level = this.baseMapper.getMemberLevelByPoint(point);

        return level != null ? level : 0;
    }
}
