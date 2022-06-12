package i2f.springcloud.spring.gateway;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;

/**
 * @author ltb
 * @date 2022/6/12 18:45
 * @desc
 */
@Slf4j
@Data
@ConditionalOnExpression("${i2f.springcloud.config.gateway.cors.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.gateway.cors")
@Configuration
public class GatewayCorsConfig {
    private String urlPatten="/**";

    private String allowOrigins="*";
    private String allowMethods="*";
    private String allowHeaders="*";

    private boolean allowCredentials=true;
    private long maxAge=6000L;

    @Bean
    public CorsWebFilter corsFilter(){
        CorsConfiguration config=new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList(allowMethods.split(",")));
        config.setAllowedOrigins(Arrays.asList(allowOrigins.split(",")));
        config.setAllowedHeaders(Arrays.asList(allowHeaders.split(",")));

        config.setAllowCredentials(allowCredentials);

        config.setMaxAge(maxAge);
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration(urlPatten,config);

        log.info("GatewayCorsConfig CorsWebFilter config done.");
        return new CorsWebFilter(source);
    }
}
