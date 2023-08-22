package i2f.springboot.exception;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@ConditionalOnClass(ServletException.class)
@Slf4j
@RestControllerAdvice
public class ServletExceptionHandler {

    @ExceptionHandler(ServletException.class)
    public ApiResp<String> servletEx(ServletException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "请求处理异常");
    }

    @ExceptionHandler(UnavailableException.class)
    public ApiResp<String> unavailableEx(UnavailableException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(503, "服务不可用");
    }
}
