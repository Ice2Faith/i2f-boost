package i2f.springboot.limit.annotations;

import i2f.springboot.limit.consts.LimitType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/1 17:51
 * @desc
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limits {
    Limit[] value() default {};
}
