package i2f.springboot.exception.handler.ext;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.servlet.enable:true}")
@Order(ServletExceptionHandler.ORDER)
@ConditionalOnClass(ServletException.class)
@Slf4j
@RestControllerAdvice
public class ServletExceptionHandler {
    public static final int ORDER = 6999;

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
