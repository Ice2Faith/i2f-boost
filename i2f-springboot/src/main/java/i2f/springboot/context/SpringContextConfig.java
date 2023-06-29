package i2f.springboot.context;

import i2f.spring.context.SpringUtil;
import i2f.spring.environment.EnvironmentUtil;
import i2f.spring.event.EventManager;
import i2f.spring.jackson.JacksonSerializerConfig;
import i2f.spring.mapping.MappingUtil;
import i2f.spring.spel.SpelExpressionResolver;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ltb
 * @date 2022/3/27 13:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.context.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.context")
@Configuration
@Import({
        SpelExpressionResolver.class,
        MappingUtil.class,
        EventManager.class,
        EnvironmentUtil.class,
        SpringUtil.class,
        JacksonSerializerConfig.class
})
public class SpringContextConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SpringContextConfig config done.");
    }
}
