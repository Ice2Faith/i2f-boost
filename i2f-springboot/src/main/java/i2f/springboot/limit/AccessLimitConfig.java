package i2f.springboot.limit;

import i2f.springboot.limit.core.AccessLimitAop;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ltb
 * @date 2022/7/2 22:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.limit.enable:true}")
@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "i2f.springboot.config.limit")
@Import({
        AccessLimitAop.class
})
public class AccessLimitConfig {
}
