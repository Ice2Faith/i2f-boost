package i2f.springboot.activity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author ltb
 * @date 2022/2/25 9:24
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.activity.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.activity")
public class ActivityConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private boolean enableJdbcConnection=false;

    private String datasourceName;

    private boolean enableAutoBuildTables=true;

    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(){
        StandaloneProcessEngineConfiguration config=new StandaloneProcessEngineConfiguration();
        if(enableJdbcConnection){
            config.setJdbcDriver(jdbcDriver);
            config.setJdbcUrl(jdbcUrl);
            config.setJdbcUsername(jdbcUsername);
            config.setJdbcPassword(jdbcPassword);
            log.info("ActivityConfig use jdbc connection.");
        }else{
            DataSource dataSource=null;
            if(datasourceName!=null && !"".equals(datasourceName)){
                dataSource=applicationContext.getBean(datasourceName,DataSource.class);
            }
            if(dataSource==null){
                dataSource=applicationContext.getBean(DataSource.class);
            }
            config.setDataSource(dataSource);
            log.info("ActivityConfig use datasource connection.");
        }
        config.setDatabaseSchemaUpdate(enableAutoBuildTables+"");
        log.info("ActivityConfig ProcessEngineConfiguration config done.");
        return config;
    }

    @Bean
    public ProcessEngine processEngine(){
        log.info("ActivityConfig ProcessEngine config done.");
        return processEngineConfiguration().buildProcessEngine();
    }

}
