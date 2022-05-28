package i2f.springcloud.alibaba.skywalking;

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
        SkyWalkingConfig.class
})
public @interface EnableSkyWalkingConfig {

}

