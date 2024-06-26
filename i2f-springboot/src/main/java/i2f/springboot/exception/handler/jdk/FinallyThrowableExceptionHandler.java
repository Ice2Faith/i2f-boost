package i2f.springboot.exception.handler.jdk;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Ice2Faith
 * @date 2023/8/23 20:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.java.enable:true}")
@Order(FinallyThrowableExceptionHandler.ORDER)
@Slf4j
@RestControllerAdvice
public class FinallyThrowableExceptionHandler {
    public static final int ORDER = 9999;

    @ExceptionHandler(Throwable.class)
    public ApiResp<String> thr(Throwable e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "抛出异常");
    }
}
