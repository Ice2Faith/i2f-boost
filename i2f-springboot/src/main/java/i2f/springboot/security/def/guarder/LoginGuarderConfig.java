package i2f.springboot.security.def.guarder;

import i2f.core.cache.ICache;
import i2f.extension.guarder.LoginGuarder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/8/22 21:58
 * @desc
 */
@ConditionalOnBean(ICache.class)
@Data
@Configuration
public class LoginGuarderConfig {
    @Bean
    public LoginGuarder loginGuarder(@Autowired ICache<String, Object> cache) {
        return new LoginGuarder("login", cache);
    }
}
