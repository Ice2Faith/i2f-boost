package i2f.springboot.security;

import i2f.springboot.security.def.DefaultAuthorizeExceptionHandler;
import i2f.springboot.security.def.DefaultUserDetailsService;
import i2f.springboot.security.def.token.*;
import i2f.springboot.security.impl.SecurityForwardController;
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
        SecurityConfig.class,PasswordEncoderConfig.class,
        SecurityExceptionHandler.class,

        DisableSecurityConfig.class,

        SecurityForwardController.class,

        DefaultAuthorizeExceptionHandler.class,
        DefaultAuthenticationTokenFilter.class,
        DefaultTokenHolder.class,
        DefaultUserDetailsService.class,
        DefaultAuthenticationSuccessHandler.class,
        DefaultAuthenticationFailureHandler.class,
        DefaultLogoutSuccessHandler.class
})
public @interface EnableSecurityConfig {

}

