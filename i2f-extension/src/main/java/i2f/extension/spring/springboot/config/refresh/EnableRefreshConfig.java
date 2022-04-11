package i2f.extension.spring.springboot.config.refresh;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/27 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        RefreshConfig.class,
        AutoRefreshConfig.class,
        RefreshController.class
})
public @interface EnableRefreshConfig {

}

