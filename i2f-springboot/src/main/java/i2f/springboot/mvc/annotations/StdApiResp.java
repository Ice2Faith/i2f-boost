package i2f.springboot.mvc.annotations;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/4/7 23:28
 * @desc
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StdApiResp {
    boolean value() default true;
}
