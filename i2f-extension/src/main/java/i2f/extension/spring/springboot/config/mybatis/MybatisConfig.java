package i2f.extension.spring.springboot.config.mybatis;

import i2f.extension.mybatis.interceptor.impl.CamelKeyResultSetInterceptor;
import i2f.extension.mybatis.interceptor.impl.SqlLogStatementInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ltb
 * @date 2022/3/27 14:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.mybatis.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.mybatis")
@MapperScan(basePackages = {"com.**.mapper","com.**.dao"})
public class MybatisConfig implements InitializingBean {

    @Value("${i2f.springboot.config.mybatis.interceptor.camel-key-pre-process.enable:true}")
    private boolean enableCamelKeyInterceptor;

    @Value("${i2f.springboot.config.mybatis.interceptor.sql-log.enable:true}")
    private boolean enableSqlLogInterceptor;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("MybatisConfig config done.");
    }

    @ConditionalOnExpression("${i2f.springboot.config.mybatis.interceptor.enable:true}")
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                log.info("MybatisConfig interceptors config");
                if(enableCamelKeyInterceptor){
                    log.info("MybatisConfig camel key interceptor config");
                    configuration.addInterceptor(new CamelKeyResultSetInterceptor());
                }
                if(enableSqlLogInterceptor){
                    log.info("MybatisConfig sql log interceptor config");
                    configuration.addInterceptor(new SqlLogStatementInterceptor());
                }
            }
        };
    }
}
