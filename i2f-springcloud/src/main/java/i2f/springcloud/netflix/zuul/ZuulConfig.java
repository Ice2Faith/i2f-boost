package i2f.springcloud.netflix.zuul;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/12 20:37
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.zuul.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.zuul")
@Configuration
@EnableZuulProxy
public class ZuulConfig {

}
