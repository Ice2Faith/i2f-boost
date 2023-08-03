package i2f.springboot.log;


import i2f.springboot.log.aop.AnnotationLogAop;
import i2f.springboot.log.aop.ApiLogAop;
import i2f.springboot.log.impl.Slf4jLogWriter;
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
        LogConfig.class,
        AnnotationLogAop.class,
        ApiLogAop.class,
        Slf4jLogWriter.class,
})
public @interface EnableLogConfig {
}
