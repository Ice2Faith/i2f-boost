package i2f.springboot.secure.customizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import i2f.spring.jackson.datetime.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.jackson.enable:true}")
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.secure.jackson")
public class SecureObjectMapperCustomizerConfig {
    @Value("${spring.jackson.date-format:}")
    private String dateFormat;

    private boolean enableLongToString = true;

    private String localDateFormat;

    private String localTimeFormat;

    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer.class)
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                if (enableLongToString) {
                    builder.serializerByType(Long.class, ToStringSerializer.instance);
                    builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
                    builder.serializerByType(long.class, ToStringSerializer.instance);
                }
                if(StringUtils.isEmpty(dateFormat)){
                    builder.deserializerByType(Date.class,new JacksonDateDeserializer());
                }
                if (!StringUtils.isEmpty(dateFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                    builder.serializerByType(LocalDateTime.class, new JacksonTemporalSerializer<LocalDateTime>(formatter));
                    builder.deserializerByType(LocalDateTime.class, new JacksonLocalDateTimeDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(localDateFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localDateFormat);
                    builder.serializerByType(LocalDate.class, new JacksonTemporalSerializer<LocalDate>(formatter));
                    builder.deserializerByType(LocalDate.class, new JacksonLocalDateDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(localTimeFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localTimeFormat);
                    builder.serializerByType(LocalTime.class, new JacksonTemporalSerializer<LocalTime>(formatter));
                    builder.deserializerByType(LocalTime.class, new JacksonLocalTimeDeserializer(formatter));
                }
            }
        };
    }
}
