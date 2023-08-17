package i2f.springboot.verifycode;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/8/15 17:19
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.verifycode.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.verifycode")
public class VerifyCodeConfig {
    private String cacheKeyPrefix = "verifycode:";
    private long expireSeconds = 60;
}
