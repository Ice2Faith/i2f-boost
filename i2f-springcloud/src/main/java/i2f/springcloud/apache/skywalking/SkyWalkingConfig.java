package i2f.springcloud.apache.skywalking;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/5/28 19:44
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.skywalking.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.skywalking")
@Configuration
public class SkyWalkingConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SkyWalkingConfig config done.");
    }
}
