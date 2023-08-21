package i2f.springboot.secure.exception;

import i2f.core.std.api.ApiResp;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Ice2Faith
 * @date 2023/6/13 18:40
 * @desc
 */
@RestControllerAdvice
@Component
public class ExceptionResolveHandler {

    @ExceptionHandler(SecureException.class)
    public Object handle(SecureException e) {
        e.printStackTrace();
        return ApiResp.error(e.code().code(), e.getMessage());
    }
}
