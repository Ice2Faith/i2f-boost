package i2f.core.net.http.rest.annotations;

import i2f.core.annotations.remark.Author;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/5/18 9:14
 * @desc
 */
@Author("i2f")
@Target({
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestHeader {
    // header name
    String name() default "";
    // header value
    String value() default "";
    // header from which parameter
    String param() default "";
    // header from which parameter's which attr
    String attr() default "";
}
