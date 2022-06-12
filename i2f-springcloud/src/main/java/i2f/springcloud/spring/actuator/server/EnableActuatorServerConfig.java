package i2f.springcloud.spring.actuator.server;

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
        ActuatorServerConfig.class
})
public @interface EnableActuatorServerConfig {

}

