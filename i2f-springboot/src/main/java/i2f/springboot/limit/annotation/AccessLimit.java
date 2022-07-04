package i2f.springboot.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2021/9/2
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    boolean value() default true;
    // 时间窗口
    long timeline() default 1L;
    // 时间窗口单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    // 单用户限制总次数
    long userCount() default 3L;
    // 所有访问限制总次数
    long apiCount() default -1;
}
