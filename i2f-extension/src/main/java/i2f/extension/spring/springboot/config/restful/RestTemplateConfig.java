package i2f.extension.spring.springboot.config.restful;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author ltb
 * @date 2022/3/4 17:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.restful.enable:true}")
@Configuration
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.restful")
public class RestTemplateConfig {

    private boolean useSimple=true;
    private int connectTimeout=30000;
    private int readTimeout=30000;

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory requestFactory;
        if(useSimple){
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(connectTimeout);
            factory.setReadTimeout(readTimeout);
            requestFactory=factory;
        }else{
            HttpComponentsClientHttpRequestFactory factory=new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(connectTimeout);// 设置连接超时，单位毫秒
            factory.setReadTimeout(readTimeout);  //设置读取超时
            requestFactory=factory;
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        log.info("RestTemplateConfig config done.");
        return restTemplate;
    }
}
