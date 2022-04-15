package i2f.springboot.advice;

import i2f.springboot.advice.impl.DefaultAesObjectJsonEncryptor;
import i2f.springboot.advice.impl.DefaultAesStringDecryptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/4/15 10:15
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        RequestBodyDecryptAdvice.class,
        ResponseBodyEncryptAdvice.class,

        DecryptRequestParamsAop.class,

        EncodeRequestForwardController.class,

        RequestResponseAdviceConfig.class,

        DefaultAesStringDecryptor.class,
        DefaultAesObjectJsonEncryptor.class
})
public @interface EnableHttpWebAdviceConfig {
}
