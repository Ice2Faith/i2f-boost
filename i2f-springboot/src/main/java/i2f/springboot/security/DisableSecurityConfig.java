package i2f.springboot.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/4/22 10:34
 * @desc
 */
@ConditionalOnExpression("!${i2f.springboot.config.security.enable:true}")
@Configuration
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class DisableSecurityConfig {
}
