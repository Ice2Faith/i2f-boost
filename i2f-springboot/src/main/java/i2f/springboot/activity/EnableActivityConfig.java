package i2f.springboot.activity;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/5/3 12:44
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        ActivityConfig.class,
        ActivityServicesConfig.class,
        ActivityManager.class
})
public @interface EnableActivityConfig {
}
