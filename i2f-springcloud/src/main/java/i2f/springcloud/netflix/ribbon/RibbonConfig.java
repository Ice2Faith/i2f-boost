package i2f.springcloud.netflix.ribbon;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/5/28 19:46
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.ribbon.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.ribbon")
@Configuration
public class RibbonConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RibbonConfig config done.");
    }
}
