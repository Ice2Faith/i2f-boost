package i2f.springboot.mq.rabbit;

import i2f.springboot.mq.rabbit.impl.RabbitConfirmCallbackLogImpl;
import i2f.springboot.mq.rabbit.impl.RabbitReturnCallbackLogImpl;
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
        RabbitMqConfig.class,
        RabbitMqManager.class,
        RabbitConfirmCallbackLogImpl.class,
        RabbitReturnCallbackLogImpl.class
})
public @interface EnableRabbitConfig {

}
