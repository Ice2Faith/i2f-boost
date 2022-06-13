package i2f.springboot.mq.kafka;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/6/13 9:42
 * @desc
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({
        KafkaConfigProvider.class,
        KafkaConfig.class,
        SpringKafkaConfig.class
})
public @interface EnableKafkaConfig {

}
