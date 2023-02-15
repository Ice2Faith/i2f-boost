package i2f.springboot.log.test.mapper;

import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.data.SysLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Ice2Faith
 * @date 2023/2/7 17:28
 * @desc
 */
@SysLog(value = false)
@Mapper
public interface DatabaseSysLogMapper {
    int insertSelective(@Param("post") SysLogDto post);
}
