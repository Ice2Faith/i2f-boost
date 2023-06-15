package i2f.springboot.secure;

import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.spring.mapping.MappingUtil;
import i2f.springboot.secure.core.*;
import i2f.springboot.secure.exception.ExceptionResolveHandler;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
        JacksonJsonProcessor.class,
        SecureTransferFilter.class,
        SecureTransferAop.class,
        SecureTransfer.class,
        MappingUtil.class,
        SecureController.class,
        SecureEncodeForwardController.class,
        ExceptionResolveHandler.class
})
@EnableWebMvc
public @interface EnableSecureConfig {

}
