package i2f.springboot.exception.handler.jdk;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;
import java.time.DateTimeException;

/**
 * @author Ice2Faith
 * @date 2023/8/23 20:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.java.enable:true}")
@Order(BasicRuntimeExceptionHandler.ORDER)
@Slf4j
@RestControllerAdvice
public class BasicRuntimeExceptionHandler {
    public static final int ORDER = 8989;

    @ExceptionHandler(NullPointerException.class)
    public ApiResp<String> nullPointerEx(NullPointerException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "空指针异常");
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ApiResp<String> indexOutOfBoundsEx(IndexOutOfBoundsException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "越界异常");
    }

    @ExceptionHandler(DateTimeException.class)
    public ApiResp<String> dateTimeEx(DateTimeException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "时间异常");
    }

    @ExceptionHandler(ArithmeticException.class)
    public ApiResp<String> arithmeticEx(ArithmeticException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "算术异常");
    }

    @ExceptionHandler(ClassCastException.class)
    public ApiResp<String> classCastEx(ClassCastException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "类转型异常");
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ApiResp<String> unsupportedOperationEx(UnsupportedOperationException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "不支持的操作异常");
    }

    @ExceptionHandler(SecurityException.class)
    public ApiResp<String> securityEx(SecurityException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "安全异常");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ApiResp<String> illegalStateEx(IllegalStateException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "非法状态异常");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResp<String> illegalArgumentEx(IllegalArgumentException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "非法参数异常");
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ApiResp<String> undeclaredThrowableEx(UndeclaredThrowableException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "内部未声明异常");
    }
}
