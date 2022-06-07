package i2f.springboot.property;

import i2f.springboot.property.core.IPropertyDecryptor;
import i2f.springboot.property.core.PropertiesDecryptAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/7 10:12
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.property.enable:true}")
@Slf4j
@ConfigurationProperties("i2f.springboot.config.property")
@Configuration
public class PropertyDecryptConfig  {

    @Autowired
    private IPropertyDecryptor propertyDecryptor;

    @Bean
    public PropertiesDecryptAdapter propertiesDecryptAdapter(){
        return new PropertiesDecryptAdapter(propertyDecryptor);
    }

}
