package i2f.springboot.exception.handler.jdk;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

/**
 * @author Ice2Faith
 * @date 2023/8/23 20:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.java.enable:true}")
@Order(BasicExceptionExceptionHandler.ORDER)
@Slf4j
@RestControllerAdvice
public class BasicExceptionExceptionHandler {
    public static final int ORDER = 8999;

    @ExceptionHandler(IOException.class)
    public ApiResp<String> ioEx(IOException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "输入输出异常");
    }

    @ExceptionHandler(SQLException.class)
    public ApiResp<String> sqlEx(SQLException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "数据库异常");
    }

    @ExceptionHandler(ParseException.class)
    public ApiResp<String> parseEx(ParseException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "解析异常");
    }

    @ExceptionHandler(TimeoutException.class)
    public ApiResp<String> timeoutEx(TimeoutException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "超时异常");
    }

    @ExceptionHandler(GeneralSecurityException.class)
    public ApiResp<String> generalSecurityEx(GeneralSecurityException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "安全异常");
    }

    @ExceptionHandler(InterruptedException.class)
    public ApiResp<String> interruptedEx(InterruptedException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "中断异常");
    }

    @ExceptionHandler(CloneNotSupportedException.class)
    public ApiResp<String> cloneNotSupportedEx(CloneNotSupportedException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "不可克隆异常");
    }
}
