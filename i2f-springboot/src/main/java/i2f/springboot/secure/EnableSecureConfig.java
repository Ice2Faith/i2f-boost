package i2f.springboot.secure;

import i2f.spring.jackson.JacksonJsonSerializer;
import i2f.spring.mapping.MappingUtil;
import i2f.springboot.secure.advice.RequestBodyDecryptAdvice;
import i2f.springboot.secure.advice.StandardApiResponseAdvice;
import i2f.springboot.secure.core.*;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2021/9/2
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        SecureConfig.class,
        JacksonJsonSerializer.class,
        SecureTransferFilter.class,
        SecureTransferAop.class,
        SecureTransfer.class,
        MappingUtil.class,
        SecureController.class,
        EncodeRequestForwardController.class,
        RequestBodyDecryptAdvice.class,
        StandardApiResponseAdvice.class
})
public @interface EnableSecureConfig {

}
