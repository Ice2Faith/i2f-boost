package i2f.springboot.swagger;

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
        SwaggerConfig.class,
        DefaultSwaggerApisConfig.class,
        CustomerSwaggerApisConfig.class
})
public @interface EnableSwaggerConfig {

}

