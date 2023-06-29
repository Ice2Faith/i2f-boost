package i2f.spring.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/6/29 15:20
 * @desc
 */
@Configuration
public class JacksonSerializerConfig {
    @Bean
    public JacksonJsonSerializer jacksonJsonSerializer(ObjectMapper objectMapper) {
        return new JacksonJsonSerializer(objectMapper);
    }
}
