package i2f.springboot.zplugin.file;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConditionalOnExpression("${i2f.springboot.config.file.enable:true}")
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.file")
public class FileConfig {

    private String storePath;
}
