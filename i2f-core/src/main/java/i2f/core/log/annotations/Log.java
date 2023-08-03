package i2f.core.log.annotations;

import i2f.core.log.consts.LogLevel;

import java.lang.annotation.*;

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
public @interface Log {
    boolean value() default true;
    boolean before() default true;
    boolean after() default false;
    boolean throwing() default true;

    LogLevel throwLevel() default LogLevel.ERROR;

    String module() default "";
    String label() default "";
}
