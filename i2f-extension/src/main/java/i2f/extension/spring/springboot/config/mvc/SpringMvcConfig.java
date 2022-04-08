package i2f.extension.spring.springboot.config.mvc;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ltb
 * @date 2022/3/27 13:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.mvc.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.mvc")
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer, InitializingBean {

    @Value("${i2f.springboot.config.mvc.param-spy-intercepter.enable:true}")
    private boolean useParamSpyIntercepter;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SpringMvcConfig config done.");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        if(useParamSpyIntercepter){
            registry.addInterceptor(new ParamsSpyInterceptor()).addPathPatterns("/**");
            System.out.println("params started.");
        }
    }
}
