package neko.convenient.nekoconvenientmember8003.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientmember8003.entity.MemberLevelDict;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientmember8003.vo.NewMemberLevelVo;
import neko.convenient.nekoconvenientmember8003.vo.UpdateMemberLevelVo;

/**
 * <p>
 * 用户等级字典表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface MemberLevelDictService extends IService<MemberLevelDict> {
    void newMemberLevel(NewMemberLevelVo vo);

    Page<MemberLevelDict> getMemberLevelDictByQueryLimitedPage(QueryVo vo);

    void updateMemberLevelDict(UpdateMemberLevelVo vo);
}
