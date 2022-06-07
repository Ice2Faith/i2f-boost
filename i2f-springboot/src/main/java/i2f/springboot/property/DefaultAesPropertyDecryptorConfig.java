package i2f.springboot.property;

import i2f.springboot.property.core.IPropertyDecryptor;
import i2f.springboot.property.impl.AesPropertyDecryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/7 10:12
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.property.aes.enable:false}")
@Configuration
public class DefaultAesPropertyDecryptorConfig {

    @Value("${i2f.springboot.config.property.aes.key}")
    private String key;

    @Bean
    public IPropertyDecryptor propertyDecryptor(){
        return new AesPropertyDecryptor(key);
    }
}
