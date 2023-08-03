package i2f.springboot.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.core.log.core.ILogWriter;
import i2f.core.log.core.LogContext;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2023/8/1 17:59
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.log.enable:true}")
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.log")
public class LogConfig implements ApplicationContextAware {
    public static ApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private String system;
    private int maxQueueSize = -1;

    private boolean defaultBefore=false;
    private boolean defaultAfter=false;
    private boolean defaultThrowing=true;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LogConfig.context = applicationContext;
        LogContext.canShutdown.set(false);
        LogContext.defaultBefore.set(defaultBefore);
        LogContext.defaultAfter.set(defaultAfter);
        LogContext.defaultThrowing.set(defaultThrowing);

        if (StringUtils.isEmpty(system)) {
            system = applicationContext.getEnvironment().getProperty("spring.application.name");
        }
        if(!StringUtils.isEmpty(system)){
            LogContext.system=this.system;
        }
        LogContext.maxQueue.set(this.maxQueueSize);
        LogContext.serializer=new JacksonJsonSerializer(objectMapper);

        Map<String,ILogWriter> writers = getBeans(ILogWriter.class);
        for (String key : writers.keySet()) {
            ILogWriter writer = writers.get(key);
            writers.put(LogContext.DEFAULT_WRITER_KEY,writer);
            break;
        }
        LogContext.writers.putAll(writers);
        LogContext.initialed.set(true);

        log.info("LogConfig config done.");
    }

    public static <T> Map<String,T> getBeans(Class<T> clazz) {
        Map<String,T> ret=new ConcurrentHashMap<>();

        String[] names = context.getBeanNamesForType(clazz);
        for (String name : names) {
            Object bean = context.getBean(name);
            ret.put(name,(T)bean);
        }
        return ret;
    }

}
