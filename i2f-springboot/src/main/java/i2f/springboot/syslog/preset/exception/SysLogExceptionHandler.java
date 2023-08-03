package i2f.springboot.syslog.preset.exception;

import i2f.springboot.syslog.SysLogger;
import i2f.springboot.syslog.SysLoggerFactory;
import i2f.springboot.syslog.annotations.SysLog;
import i2f.springboot.syslog.consts.LogType;
import i2f.springboot.syslog.util.SysLogUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ice2Faith
 * @date 2023/2/8 14:17
 * @desc
 */
@Order(-1)
@SysLog(value = false)
@ConditionalOnExpression("${sys.log.exception.handler.enable:false}")
@Component
public class SysLogExceptionHandler implements HandlerExceptionResolver {
    private SysLogger logger = SysLoggerFactory.getLogger(SysLogExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        logger.error("exception-handler", (dto) -> {
            SysLogUtil.fillTraceId(dto);
            SysLogUtil.fillLogExceptionTrackByException(dto, e);
            SysLogUtil.fillRequestInfosByRequestContextHolder(dto);
            SysLogUtil.fillSystemModuleByEnvironment(dto);
            SysLogUtil.fillLogContentByArgs(dto, e.getMessage());
            dto.setLogType(LogType.EXCEPTION.code());
        });
        return null;
    }
}
