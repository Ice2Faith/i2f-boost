package i2f.extension.spring.springboot.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ltb
 * @date 2022/4/7 10:53
 * @desc
 */
@ConditionalOnMissingBean(PasswordEncoder.class)
@Slf4j
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("PasswordEncoderConfig BCryptPasswordEncoder config.");
        return new BCryptPasswordEncoder();
    }
}
