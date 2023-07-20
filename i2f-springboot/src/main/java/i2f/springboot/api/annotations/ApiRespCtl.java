package i2f.springboot.api.annotations;

import i2f.springboot.api.ApiRespConfig;
import i2f.springboot.api.ApiRespResponseBodyAdvice;
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
public @interface ApiRespCtl {
    boolean value() default true;
}
