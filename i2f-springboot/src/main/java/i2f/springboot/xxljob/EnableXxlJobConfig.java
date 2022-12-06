package i2f.springboot.xxljob;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/12/06 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        XxlJobConfig.class
})
public @interface EnableXxlJobConfig {

}

