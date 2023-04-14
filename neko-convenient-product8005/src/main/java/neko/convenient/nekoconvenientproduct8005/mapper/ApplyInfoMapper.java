package neko.convenient.nekoconvenientproduct8005.mapper;

import neko.convenient.nekoconvenientproduct8005.entity.ApplyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 品牌申请表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface ApplyInfoMapper extends BaseMapper<ApplyInfo> {
    void updateStatusByApplyId(String applyId,
                               String applyAdminId,
                               Byte status,
                               String statusInfo,
                               LocalDateTime updateTime);
}
