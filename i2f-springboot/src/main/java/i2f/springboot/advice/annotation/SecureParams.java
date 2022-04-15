package i2f.springboot.advice.annotation;

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
        ElementType.TYPE,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureParams {
    boolean in() default true;

    boolean out() default true;
}
