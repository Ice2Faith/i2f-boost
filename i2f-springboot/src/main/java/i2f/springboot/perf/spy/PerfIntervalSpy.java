package i2f.springboot.perf.spy;

import i2f.core.thread.NamingThreadFactory;
import i2f.springboot.perf.PerfConfig;
import i2f.springboot.perf.context.PerfStorage;
import i2f.springboot.perf.data.PerfIndex;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Data
@ConditionalOnExpression("${i2f.springboot.config.perf.collect.interval.enable:true}")
@Component
public class PerfIntervalSpy implements ApplicationContextAware, InitializingBean {
    private ConcurrentHashMap<String, PerfSpy> spyMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PerfMultiSpy> spyMultiMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(3,new NamingThreadFactory("perf","interval"));

    @Value("${i2f.springboot.config.perf.collect.interval.seconds:5}")
    private int intervalSeconds=5;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, PerfSpy> entry : spyMap.entrySet()) {
                PerfSpy spy = entry.getValue();
                String name = spy.name();
                double value = spy.collect();
                PerfConfig.STORE.add(name, value);
            }
            for (Map.Entry<String, PerfMultiSpy> entry : spyMultiMap.entrySet()) {
                PerfMultiSpy spy = entry.getValue();
                List<PerfIndex> idx = spy.collect();
                for (PerfIndex item : idx) {
                    PerfConfig.STORE.add(item.getName(), item.getValue());
                }
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        String[] beanNames = context.getBeanNamesForType(PerfSpy.class);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            spyMap.put(beanName, (PerfSpy) bean);
        }
        String[] beanMultiNames = context.getBeanNamesForType(PerfMultiSpy.class);
        for (String beanName : beanMultiNames) {
            Object bean = context.getBean(beanName);
            spyMultiMap.put(beanName, (PerfMultiSpy) bean);
        }
    }
}
