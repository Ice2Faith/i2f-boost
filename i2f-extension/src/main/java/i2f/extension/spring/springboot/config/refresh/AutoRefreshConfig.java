package i2f.extension.spring.springboot.config.refresh;

import i2f.core.thread.ArgsRunnable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/11 8:31
 * @desc
 */
@ConditionalOnBean(RefreshConfig.class)
@ConditionalOnExpression("${i2f.springboot.config.refresh.auto-refresh.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.refresh.auto-refresh")
public class AutoRefreshConfig implements InitializingBean {
    private ScheduledExecutorService pool;

    @Autowired
    private RefreshConfig refreshConfig;

    private int delayTime=5;

    private String delayTimeUnit="MINUTES";

    @Override
    public void afterPropertiesSet() throws Exception {
        if(pool!=null){
            log.warn("AutoRefreshConfig refresh schedule pool has exists,stop before and rebuild.");
            pool=null;
        }
        pool=Executors.newSingleThreadScheduledExecutor();
        if(delayTime<=0){
            log.warn("AutoRefreshConfig delay-time could not lower than zero.");
            return;
        }
        TimeUnit timeUnit=TimeUnit.MILLISECONDS;
        if(delayTimeUnit!=null){
            delayTimeUnit=delayTimeUnit.trim().toUpperCase();
        }
        if("NANOSECONDS".equals(delayTimeUnit)){
            timeUnit=TimeUnit.NANOSECONDS;
        }else  if("MICROSECONDS".equals(delayTimeUnit)){
            timeUnit=TimeUnit.MICROSECONDS;
        }else  if("MILLISECONDS".equals(delayTimeUnit)){
            timeUnit=TimeUnit.MILLISECONDS;
        }else  if("SECONDS".equals(delayTimeUnit)){
            timeUnit=TimeUnit.SECONDS;
        }else  if("MINUTES".equals(delayTimeUnit)){
            timeUnit=TimeUnit.MINUTES;
        }else  if("HOURS".equals(delayTimeUnit)){
            timeUnit=TimeUnit.HOURS;
        }else  if("DAYS".equals(delayTimeUnit)){
            timeUnit=TimeUnit.DAYS;
        }

        pool.scheduleWithFixedDelay(new ArgsRunnable(delayTime,timeUnit) {
            @Override
            public void doRun(Object ... args) {
                log.info("AutoRefreshConfig schedule refresh configs in delay:"+args[0]+" of "+args[1]+" ...");
                refreshConfig.refresh();
                log.info("AutoRefreshConfig schedule refresh configs done.");
            }
        },delayTime,delayTime,timeUnit);

        log.info("AutoRefreshConfig schedule refresh configs done is:"+delayTime+" of "+timeUnit);
    }
}
