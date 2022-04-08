package i2f.extension.spring.springboot.config.security;

import i2f.extension.spring.springboot.config.security.def.*;
import i2f.extension.spring.springboot.config.security.def.token.DefaultAuthenticationSuccessHandler;
import i2f.extension.spring.springboot.config.security.def.token.DefaultAuthenticationTokenFilter;
import i2f.extension.spring.springboot.config.security.def.token.DefaultLogoutSuccessHandler;
import i2f.extension.spring.springboot.config.security.def.token.DefaultTokenHolder;
import i2f.extension.spring.springboot.config.security.impl.*;
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

