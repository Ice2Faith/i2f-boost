package i2f.springboot.log.preset.exception;

import i2f.springboot.log.SysLogger;
import i2f.springboot.log.SysLoggerFactory;
import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.consts.LogType;
import i2f.springboot.log.util.SysLogUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/2/8 14:25
 * @desc
 */
@Order(-1)
@SysLog(value = false)
@ConditionalOnExpression("${sys.log.exception.filter.enable:false}")
@Component
public class SysLogExceptionFilter extends OncePerRequestFilter {
    private SysLogger logger = SysLoggerFactory.getLogger(SysLogExceptionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (Throwable e) {
            logger.error("exception-filter", (dto) -> {
                SysLogUtil.fillTraceId(dto);
                SysLogUtil.fillLogExceptionTrackByException(dto, e);
                SysLogUtil.fillRequestInfosByRequestContextHolder(dto);
                SysLogUtil.fillSystemModuleByEnvironment(dto);
                SysLogUtil.fillLogContentByArgs(dto, e.getMessage());
                dto.setLogType(LogType.EXCEPTION.code());
            });
            throw e;
        }
    }
}
