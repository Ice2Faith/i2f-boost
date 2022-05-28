package i2f.springcloud.alibaba.sentinel;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/5/28 19:40
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.sentinel.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.sentinel")
@Configuration
public class SentinelConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SentinelConfig config done.");
    }
}
