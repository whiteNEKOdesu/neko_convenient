package neko.convenient.nekoconvenientmember8003.mapper;

import neko.convenient.nekoconvenientmember8003.entity.MemberInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface MemberInfoMapper extends BaseMapper<MemberInfo> {
    MemberInfo getMemberInfoByUserName(String userName);

    void updatePointByUid(String uid,
                          Integer point,
                          LocalDateTime updateTime);

    void updateLevelByUid(String uid,
                          LocalDateTime updateTime);
}
