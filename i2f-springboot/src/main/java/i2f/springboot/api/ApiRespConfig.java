package i2f.springboot.api;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ltb
 * @date 2022/7/2 22:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.api-resp.enable:true}")
@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "i2f.springboot.config.api-resp")
public class ApiRespConfig implements InitializingBean {
    private boolean global=true;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ApiRespConfig config done.");
    }
}
