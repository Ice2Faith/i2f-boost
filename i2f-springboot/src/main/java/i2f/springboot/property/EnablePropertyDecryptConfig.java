package i2f.springboot.property;

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
        DefaultAesPropertyDecryptorConfig.class,
        DefaultPropertyDecryptorConfig.class,
        PropertyDecryptConfig.class,
})
public @interface EnablePropertyDecryptConfig {

}

