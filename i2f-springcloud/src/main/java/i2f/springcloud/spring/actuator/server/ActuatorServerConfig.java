package i2f.springcloud.spring.actuator.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/12 20:37
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.actuator-server.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.actuator-server")
@Configuration
@EnableAdminServer
public class ActuatorServerConfig {

}
