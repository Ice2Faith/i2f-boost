package i2f.springboot.secure;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/29 15:50
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.enable:true}")
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.secure")
public class SecureConfig {
    private String rsaStorePath="../";

    private String whiteAntPaths;

    private String responseCharset;

    private int rsaKeySize=1024;

    private boolean enableDynamicRsaKey=true;

    private long dynamicRefreshDelaySeconds=6*60;

    private int dynamicMaxHistoriesCount=5;
}
