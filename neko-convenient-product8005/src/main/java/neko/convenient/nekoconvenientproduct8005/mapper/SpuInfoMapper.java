package neko.convenient.nekoconvenientproduct8005.mapper;

import neko.convenient.nekoconvenientproduct8005.entity.SpuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * spu信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2023-04-01
 */
@Mapper
public interface SpuInfoMapper extends BaseMapper<SpuInfo> {
    void updateSpuIsPublishBySpuId(String spuId, Boolean isPublish, LocalDateTime updateTime);
}
