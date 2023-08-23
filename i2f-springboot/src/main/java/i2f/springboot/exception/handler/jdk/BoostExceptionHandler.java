package i2f.springboot.exception.handler.jdk;

import i2f.core.exception.BoostException;
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
@ConditionalOnExpression("${i2f.springboot.config.exception.boost.enable:true}")
@Order(BoostExceptionHandler.ORDER)
@ConditionalOnClass(BoostException.class)
@Slf4j
@RestControllerAdvice
public class BoostExceptionHandler {
    public static final int ORDER = 7999;

    @ExceptionHandler(BoostException.class)
    public ApiResp<String> boostEx(BoostException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, e.getMessage());
    }

}
