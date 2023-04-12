package neko.convenient.nekoconvenientproduct8005.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.convenient.nekoconvenientcommonbase.utils.entity.QueryVo;
import neko.convenient.nekoconvenientproduct8005.entity.ApplyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.convenient.nekoconvenientproduct8005.vo.ApplyInfoVo;

/**
 * <p>
 * 品牌申请表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
public interface ApplyInfoService extends IService<ApplyInfo> {
    void applyBrand(ApplyInfoVo vo);

    Page<ApplyInfo> getApplyInfoByQueryLimitedPage(QueryVo vo);
}
