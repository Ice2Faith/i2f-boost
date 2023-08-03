package i2f.springboot.syslog.core;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/2/7 16:15
 * @desc
 */
@Component
public class SpringContextProvider implements ApplicationContextAware, EnvironmentAware, ApplicationRunner {

    private static ApplicationContext applicationContext;
    private static Environment environment;
    public static AtomicBoolean inited = new AtomicBoolean(false);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextProvider.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringContextProvider.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        inited.set(true);
    }

    public static String getEnv(String key) {
        if (environment != null) {
            return environment.getProperty(key);
        }
        return null;
    }

    public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
        Map<String, T> map = new ConcurrentHashMap<>();
        if (applicationContext == null) {
            return map;
        }
        String[] names = applicationContext.getBeanNamesForType(clazz);
        for (String name : names) {
            T handler = (T) applicationContext.getBean(name);
            map.put(name, handler);
        }
        return map;
    }

}
