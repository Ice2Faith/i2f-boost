package i2f.springboot.quartz;

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
        QuartzConfig.class,
        JobFactory.class,
        QuartzScannerConfig.class
})
public @interface EnableQuartzConfig {

}

