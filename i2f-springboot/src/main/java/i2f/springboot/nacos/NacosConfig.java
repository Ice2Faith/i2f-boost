package i2f.springboot.nacos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/3/27 13:40
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springboot.config.nacos.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.config.nacos")
@Configuration
@EnableDiscoveryClient
public class NacosConfig {

}
