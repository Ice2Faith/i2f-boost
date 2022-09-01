package i2f.springboot.property;

import i2f.springboot.property.core.IPropertyDecryptor;
import i2f.springboot.property.impl.Base64PropertyDecryptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/7 10:12
 * @desc
 */
@ConditionalOnExpression("!${i2f.springboot.config.property.aes.enable:false}")
@Configuration
public class DefaultPropertyDecryptorConfig {

    @Bean
    public IPropertyDecryptor propertyDecryptor(){
        return new Base64PropertyDecryptor();
    }
}
