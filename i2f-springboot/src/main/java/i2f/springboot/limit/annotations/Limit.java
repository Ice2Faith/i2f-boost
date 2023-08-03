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
@Repeatable(Limits.class)
public @interface Limit {
    boolean value() default true;

    int window() default 1;
    TimeUnit unit() default TimeUnit.SECONDS;

    int count() default -1;

    LimitType type() default LimitType.GLOBAL;
}
