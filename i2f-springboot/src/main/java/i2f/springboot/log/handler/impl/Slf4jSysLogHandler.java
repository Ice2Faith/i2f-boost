package i2f.springboot.log.handler.impl;

import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.consts.LogLevel;
import i2f.springboot.log.data.SysLogDto;
import i2f.springboot.log.handler.SysLogHandler;
import i2f.springboot.log.util.SysLogUtil;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:44
 * @desc
 */
@SysLog(value = false)
@ConditionalOnExpression("${sys.log.slf4j.handler.enable:true}")
@ConditionalOnClass(name = "org.slf4j.Logger")
@Component
public class Slf4jSysLogHandler implements SysLogHandler {

    @Override
    public void handle(SysLogDto dto) {
        Logger log = SysLogUtil.getSlf4jLogger(dto.getLogLocation());
        Integer level = dto.getLogLevel();
        if (level == null) {
            level = LogLevel.INFO.code();
        }

        if (LogLevel.WARN.code() == level) {
            if (log.isWarnEnabled()) {
                log.warn(SysLogUtil.log2str(dto));
            }
        } else if (LogLevel.ERROR.code() == level) {
            if (log.isErrorEnabled()) {
                log.error(SysLogUtil.log2str(dto));
            }
        } else if (LogLevel.DEBUG.code() == level) {
            if (log.isDebugEnabled()) {
                log.debug(SysLogUtil.log2str(dto));
            }
        } else if (LogLevel.TRACE.code() == level) {
            if (log.isTraceEnabled()) {
                log.trace(SysLogUtil.log2str(dto));
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info(SysLogUtil.log2str(dto));
            }
        }
    }


}
