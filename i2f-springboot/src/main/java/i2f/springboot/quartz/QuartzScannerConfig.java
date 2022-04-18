package i2f.springboot.quartz;

import i2f.extension.quartz.driven.QuartzScanner;
import i2f.extension.quartz.driven.model.QuartzJobMeta;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/18 15:34
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.quartz.scanner.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.quartz.scanner")
public class QuartzScannerConfig implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ApplicationContext applicationContext;

    private String basePackages;

    public void scanSchedules() throws Exception {
        if(basePackages==null || "".equals(basePackages)){
            log.info("QuartzScannerConfig no schedule base packages find.");
            return;
        }
        List<QuartzJobMeta> metaList= QuartzScanner.scanBasePackage(basePackages.split(","));
        for(QuartzJobMeta item : metaList){
            log.info("QuartzScannerConfig schedule:"+item.getGroup()+"(group),"+item.getName()+"(name),"+item.getRunClassName()+"."+item.getRunMethodName()+"(method)");
            Class clazz=item.getRunClass();
            Map<String,Object> beans=applicationContext.getBeansOfType(clazz);
            if(beans.size()==1){
                String key=beans.keySet().iterator().next();
                Object obj=beans.get(key);
                log.info("QuartzScannerConfig use context bean:"+key+" for job:"+item.getRunClassName()+"."+item.getRunMethodName());
                item.setInvokeObj(obj);
            }
            QuartzScanner.makeSchedule(scheduler,item);
        }
        log.info("QuartzScannerConfig find schedule config done of count:"+metaList.size());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        scanSchedules();
    }
}
