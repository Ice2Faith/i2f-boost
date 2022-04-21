package i2f.springboot.shiro;

import i2f.springboot.shiro.def.DefaultUsernamePasswordRealm;
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
        ShiroConfig.class,

        DefaultUsernamePasswordRealm.class
})
public @interface EnableShiroConfig {

}

