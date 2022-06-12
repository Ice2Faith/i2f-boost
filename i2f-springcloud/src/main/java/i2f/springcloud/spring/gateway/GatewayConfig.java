package i2f.springcloud.spring.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author ltb
 * @date 2022/5/28 19:41
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.gateway.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.gateway")
@Configuration
@Order(-1)
public class GatewayConfig implements InitializingBean {
    private boolean enableAccessLog=false;
    public static final String GATEWAY_ACCESS_LOG_PROPERTY_NAME="reactor.netty.http.server.accessLogEnabled";

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("GatewayConfig config done.");
        System.setProperty(GATEWAY_ACCESS_LOG_PROPERTY_NAME,enableAccessLog+"");
    }
}
