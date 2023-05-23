package i2f.springboot.trace.spy.plugin;

import i2f.springboot.trace.spy.SpringTraceLogger;
import i2f.springboot.trace.spy.core.CglibTraceInvocationHandler;
import i2f.springboot.trace.spy.core.InvokeTrace;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.sql.DataSource;
import javax.websocket.server.ServerEndpoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2023/5/23 15:32
 * @desc
 */
@ConditionalOnExpression("${invoke.trace.cglib.enable:false}")
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "invoke.trace.cglib")
public class CglibProxyTrace implements BeanPostProcessor, ApplicationListener<ApplicationPreparedEvent> {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    public static ConcurrentHashMap<String, Object> enhancerBeans = new ConcurrentHashMap<>();

    private AntPathMatcher classNameMatcher = new AntPathMatcher(".");

    private List<Class<?>> skipTypes = Arrays.asList(
            DataSource.class,
            AopTrace.class,
            FilterTrace.class,
            CglibProxyTrace.class
    );

    private List<Class<? extends Annotation>> skipWithAnnotationTypes = Arrays.asList(
            Import.class,
            ServerEndpoint.class
    );

    private List<String> includeClassPattens = Arrays.asList(
            "org.jeecg.**"
    );

    {
        InvokeTrace.enable.set(false);
    }

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        InvokeTrace.enable.set(true);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class<?> clazz = bean.getClass();
        if (!Modifier.isPublic(clazz.getModifiers())) {
            return bean;
        }

        for (Class<?> item : skipTypes) {
            if (item.isAssignableFrom(clazz)) {
                return bean;
            }
        }

        for (Class<? extends Annotation> item : skipWithAnnotationTypes) {
            if (clazz.isAnnotationPresent(item)) {
                return bean;
            }
        }

        String clazzName = clazz.getName();
        boolean isTarget = false;
        for (String item : includeClassPattens) {
            if (classNameMatcher.match(item, clazzName)) {
                isTarget = true;
                break;
            }
        }
        if (!isTarget) {
            return bean;
        }

        try {

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(new CglibTraceInvocationHandler(bean, SpringTraceLogger.LOGGER));

            Object newBean = enhancer.create();

            BeanDefinition definition = defaultListableBeanFactory.getBeanDefinition(beanName);
            defaultListableBeanFactory.removeBeanDefinition(beanName);
            defaultListableBeanFactory.registerBeanDefinition(beanName, definition);

            beanFactory.autowireBean(newBean);

            enhancerBeans.put(beanName, newBean);
            return newBean;
        } catch (Throwable e) {
            log.debug("trace proxy config error: " + e.getMessage());
        }


        return bean;
    }

}
