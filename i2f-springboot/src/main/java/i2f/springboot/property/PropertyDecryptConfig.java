package i2f.springboot.property;

import i2f.springboot.property.impl.Base64PropertyDecryptor;
import i2f.springboot.property.core.IPropertyDecryptor;
import i2f.springboot.property.core.PropertiesDecryptAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/6/7 10:12
 * @desc
 */
@Configuration
public class PropertyDecryptConfig {

    @Bean
    public IPropertyDecryptor propertyDecryptor(){
        return new Base64PropertyDecryptor();
    }

    @Bean
    public PropertiesDecryptAdapter propertiesDecryptAdapter(){
        return new PropertiesDecryptAdapter(propertyDecryptor());
    }
}
