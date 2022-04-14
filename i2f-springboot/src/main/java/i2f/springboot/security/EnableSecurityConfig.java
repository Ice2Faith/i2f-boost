package i2f.springboot.security;

import i2f.springboot.security.def.DefaultUnAuthorizedHandler;
import i2f.springboot.security.def.DefaultUserDetailsService;
import i2f.springboot.security.def.token.DefaultAuthenticationSuccessHandler;
import i2f.springboot.security.def.token.DefaultAuthenticationTokenFilter;
import i2f.springboot.security.def.token.DefaultLogoutSuccessHandler;
import i2f.springboot.security.def.token.DefaultTokenHolder;
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

        SecurityForwardController.class,

        DefaultUnAuthorizedHandler.class,
        DefaultAuthenticationTokenFilter.class,
        DefaultTokenHolder.class,
        DefaultUserDetailsService.class,
        DefaultAuthenticationSuccessHandler.class,
        DefaultLogoutSuccessHandler.class
})
public @interface EnableSecurityConfig {

}

