package i2f.core.zplugin.log.annotations;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.zplugin.log.enums.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/28 8:57
 * @desc
 */
@Author("i2f")
@Remark({
        "using to point custom validators for validate arguments or other object",
        "when using on method then check method return value"
})
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    @Remark("when value is true will output log")
    boolean value() default true;
    String system() default "";
    String module() default "";
    String label() default "";
    LogLevel level() default LogLevel.INFO;
    @Remark("when before is true will output before log")
    boolean before() default true;
    boolean after() default true;
    boolean except() default true;
}
