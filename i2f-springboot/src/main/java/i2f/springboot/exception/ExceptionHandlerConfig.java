package i2f.springboot.exception;

import i2f.springboot.exception.handler.ext.JacksonExceptionHandler;
import i2f.springboot.exception.handler.ext.ServletExceptionHandler;
import i2f.springboot.exception.handler.ext.SpringMvcExceptionHandler;
import i2f.springboot.exception.handler.jdk.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ltb
 * @date 2022/7/2 22:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.enable:true}")
@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "i2f.springboot.config.exception")
@Import({
        RegistryExceptionHandler.class,

        FinallyThrowableExceptionHandler.class,
        FinallyExceptionExceptionHandler.class,
        FinallyErrorExceptionHandler.class,
        FinallyRuntimeExceptionHandler.class,

        BasicExceptionExceptionHandler.class,
        BasicRuntimeExceptionHandler.class,
        BasicJavaExceptionHandler.class,
        BasicJavaxCryptoExceptionHandler.class,

        BoostExceptionHandler.class,

        ServletExceptionHandler.class,

        SpringMvcExceptionHandler.class,
        JacksonExceptionHandler.class
})
public class ExceptionHandlerConfig {
}
