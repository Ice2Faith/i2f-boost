package i2f.springboot.quartz;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author ltb
 * @date 2022/3/27 14:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.quartz.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.quartz")
public class QuartzConfig implements InitializingBean {

    private boolean overwriteExistingJobs=true;
    private int startupDelay=1;
    private String configLocation="/quartz.properties";

    @Autowired
    private JobFactory jobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean factoryBean=new SchedulerFactoryBean();
        factoryBean.setJobFactory(jobFactory);
        // 集群时，更新已存在的job
        factoryBean.setOverwriteExistingJobs(overwriteExistingJobs);
        factoryBean.setStartupDelay(startupDelay);

        Resource rs=null;
        String[] configFinds={
                configLocation,
                "/application-quartz.properties",
                "/application.properties"
        };
        for(String item : configFinds){
            if(item==null || "".equals(item)){
                continue;
            }
            try{
                rs=new ClassPathResource(item);
                if(rs!=null){
                    log.info("QuartzConfig find config location:"+item);
                    break;
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        if(rs!=null){
            log.info("QuartzConfig find config location as quartz properties");
            factoryBean.setConfigLocation(rs);
        }
        log.info("QuartzConfig schedulerFactoryBean config done.");
        return factoryBean;
    }


    @Bean
    public Scheduler scheduler(){
        log.info("QuartzConfig scheduler config done.");
        return schedulerFactoryBean().getScheduler();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("QuartzConfig config done.");
    }
}
