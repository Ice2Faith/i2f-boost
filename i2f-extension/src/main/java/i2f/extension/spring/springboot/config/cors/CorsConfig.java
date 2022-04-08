package i2f.extension.spring.springboot.config.cors;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author ltb
 * @date 2022/3/27 13:40
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springboot.config.cors.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.config.cors")
@Configuration
public class CorsConfig {
    private String allowedOrigin="*";
    private String allowedHeader="*";
    private boolean allowCredentials=true;
    private String allowedMethod="*";
    private long maxAge=6000L;
    private String matchPatten="/**";

    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedHeader(allowedHeader);
        config.setAllowCredentials(allowCredentials);
        config.addAllowedMethod(allowedMethod);
        config.setMaxAge(maxAge);
        // 对接口配置跨域设置
        source.registerCorsConfiguration(matchPatten, config);
        log.info("CorsConfig CorsFilter config done.");
        return new CorsFilter(source);
    }
}
