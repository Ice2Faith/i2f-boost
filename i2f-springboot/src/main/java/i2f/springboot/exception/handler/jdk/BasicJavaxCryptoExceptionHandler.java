package i2f.springboot.exception.handler.jdk;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.javax-crypto.enable:true}")
@Order(BasicJavaxCryptoExceptionHandler.ORDER)
@ConditionalOnClass(Cipher.class)
@Slf4j
@RestControllerAdvice
public class BasicJavaxCryptoExceptionHandler {
    public static final int ORDER = 8969;

    @ExceptionHandler(BadPaddingException.class)
    public ApiResp<String> badPaddingEx(BadPaddingException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "错误填充异常");
    }

    @ExceptionHandler(IllegalBlockSizeException.class)
    public ApiResp<String> illegalBlockSizeEx(IllegalBlockSizeException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "非法块大小异常");
    }

    @ExceptionHandler(NoSuchPaddingException.class)
    public ApiResp<String> noSuchPaddingEx(NoSuchPaddingException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "无填充异常");
    }

}
