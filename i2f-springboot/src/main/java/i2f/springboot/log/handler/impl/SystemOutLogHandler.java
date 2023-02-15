package i2f.springboot.log.handler.impl;

import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.consts.LogLevel;
import i2f.springboot.log.data.SysLogDto;
import i2f.springboot.log.handler.SysLogHandler;
import i2f.springboot.log.util.SysLogUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:44
 * @desc
 */
@SysLog(value = false)
@ConditionalOnExpression("${sys.log.sysout.handler.enable:true}")
@ConditionalOnMissingClass(value = "org.slf4j.Logger")
@Component
public class SystemOutLogHandler implements SysLogHandler {

    @Override
    public void handle(SysLogDto dto) {
        Integer level = dto.getLogLevel();
        if (level == null) {
            level = LogLevel.INFO.code();
        }

        if (LogLevel.WARN.code() == level
                || LogLevel.ERROR.code() == level) {
            System.err.println(SysLogUtil.log2str(dto));
        } else {
            System.out.println(SysLogUtil.log2str(dto));
        }
    }

}
