package i2f.springboot.limit;

import i2f.springboot.limit.aop.ApiLimitAop;
import i2f.springboot.limit.core.LimitContext;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2023/8/1 17:51
 * @desc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        LimitConfig.class,
        LimitContext.class,
        ApiLimitAop.class
})
public @interface EnableLimitConfig {
}
