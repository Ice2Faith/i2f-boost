package i2f.springboot.shiro;

import i2f.springboot.security.impl.SecurityForwardController;
import i2f.springboot.shiro.def.*;
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
        ShiroExceptionHandler.class,

        DefaultUsernamePasswordRealm.class,

        SecurityForwardController.class,

        DefaultCustomerTokenRealm.class,
        DefaultShiroTokenHolder.class,

        DefaultLoginFailureHandler.class,
        DefaultLoginSuccessHandler.class,
        DefaultLogoutHandler.class
})
public @interface EnableShiroConfig {

}

