package i2f.springboot.mvc;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/27 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        SpringMvcConfig.class,
        ObjectMapperConfig.class,
        LogAop.class,
        PublicExceptionResolver.class
})
public @interface EnableMvcConfig {

}

