package i2f.springboot.secure.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> ret = new HashMap<>();
        ret.put("code", e.code());
        ret.put("msg", e.getMessage());
        e.printStackTrace();
        return ret;
    }
}
