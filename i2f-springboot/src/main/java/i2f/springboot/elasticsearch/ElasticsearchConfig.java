package i2f.springboot.elasticsearch;

import i2f.extension.elasticsearch.EsManager;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/5/8 21:51
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.elasticsearch.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.elasticsearch")
public class ElasticsearchConfig {
    private String connectString="localhost:9200";

    @Bean
    public EsManager esManager(){
        return EsManager.manager(connectString);
    }
}
