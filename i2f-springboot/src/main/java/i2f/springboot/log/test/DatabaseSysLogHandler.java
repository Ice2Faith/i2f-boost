package i2f.springboot.log.test;

import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.data.SysLogDto;
import i2f.springboot.log.handler.SysLogHandler;
import i2f.springboot.log.test.mapper.DatabaseSysLogMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Ice2Faith
 * @date 2023/2/7 17:31
 * @desc
 */
@SysLog(value = false)
@Component
public class DatabaseSysLogHandler implements SysLogHandler {

    @Resource
    private DatabaseSysLogMapper sysLogMapper;

    @Override
    public void handle(SysLogDto log) {
        sysLogMapper.insertSelective(log);
    }
}
