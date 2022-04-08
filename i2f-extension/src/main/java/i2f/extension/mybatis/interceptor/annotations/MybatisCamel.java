package i2f.extension.mybatis.interceptor.annotations;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/4/4 20:42
 * @desc
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MybatisCamel {
    boolean value() default true;
}
