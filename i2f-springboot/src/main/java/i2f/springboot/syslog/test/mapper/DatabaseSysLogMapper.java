package i2f.springboot.syslog.test.mapper;

import i2f.springboot.syslog.annotations.SysLog;
import i2f.springboot.syslog.data.SysLogDto;
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
