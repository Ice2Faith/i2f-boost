package i2f.springboot.log.annotations;


import i2f.springboot.log.consts.LogLevel;
import i2f.springboot.log.consts.LogOperateType;
import i2f.springboot.log.consts.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:05
 * @desc
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {
    boolean value() default true;

    String system() default "";

    String module() default "";

    String label() default "";

    LogOperateType operate() default LogOperateType.QUERY;

    LogLevel level() default LogLevel.INFO;

    LogType type() default LogType.API;
}
