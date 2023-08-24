package i2f.springboot.exception.handler.ext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.jackson.enable:true}")
@Order(JacksonExceptionHandler.ORDER)
@ConditionalOnClass(JsonProcessingException.class)
@Slf4j
@RestControllerAdvice
public class JacksonExceptionHandler {
    public static final int ORDER = 5999;

    @ExceptionHandler(JsonParseException.class)
    public ApiResp<String> jsonParseEx(JsonParseException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "JSON解析异常");
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ApiResp<String> jsonProcessingEx(JsonProcessingException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "JSON处理异常");
    }

}
