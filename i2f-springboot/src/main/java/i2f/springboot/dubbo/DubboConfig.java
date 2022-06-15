package i2f.springboot.dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/3/4 17:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.dubbo.enable:true}")
@Configuration
@Slf4j
@Data
@EnableDubbo
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.dubbo")
public class DubboConfig {

}
