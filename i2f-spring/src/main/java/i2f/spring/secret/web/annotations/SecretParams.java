package i2f.spring.secret.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/10/20 16:49
 * @desc
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE,
        ElementType.PARAMETER,
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecretParams {
    boolean in() default true;

    boolean out() default true;
}

