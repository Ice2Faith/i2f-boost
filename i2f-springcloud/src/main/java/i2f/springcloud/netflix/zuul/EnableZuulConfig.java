package i2f.springcloud.netflix.zuul;

import i2f.springcloud.netflix.zuul.filter.ZuulResponseCharsetFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/5/28 17:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        ZuulConfig.class,
        ZuulResponseCharsetFilter.class
})
public @interface EnableZuulConfig {

}

