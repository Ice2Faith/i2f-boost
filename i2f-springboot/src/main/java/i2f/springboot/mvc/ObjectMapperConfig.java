package i2f.springboot.mvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.spring.jackson.JacksonJsonProcessor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author ltb
 * @date 2022/3/27 13:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.mvc.object-mapper.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.mvc.object-mapper")
@Configuration
public class ObjectMapperConfig {

    private String dateFormat;
    private boolean includeNull=true;

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        if(includeNull){
            objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        }else{
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        log.info("ObjectMapper config done.");
        return objectMapper;
    }

    @Bean
    public JacksonJsonProcessor getJacksonJsonProcessor(){
        log.info("JacksonJsonProcessor config done.");
        return new JacksonJsonProcessor(getObjectMapper());
    }

}
