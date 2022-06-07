package i2f.springboot.property.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author ltb
 * @date 2022/6/7 9:04
 * @desc
 */
public class PropertiesDecryptAdapter implements BeanFactoryPostProcessor, EnvironmentAware, ApplicationListener<ApplicationEvent>, Ordered {
    private Environment env;
    private ConfigurableListableBeanFactory beanFactory;
    private IPropertyDecryptor decryptor;
    public PropertiesDecryptAdapter(IPropertyDecryptor decryptor){
        this.decryptor=decryptor;
    }
    @Override
    public void setEnvironment(Environment env) {
        this.env=env;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
        wrapEnv(env,beanFactory);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        wrapEnv(env,beanFactory);
    }

    @Override
    public int getOrder() {
        return -9999;
    }

    public void wrapEnv(Environment env,ConfigurableListableBeanFactory beanFactory){
        if(!(env instanceof ConfigurableEnvironment)){
            return;
        }
        ConfigurableEnvironment cenv=(ConfigurableEnvironment)env;

        MutablePropertySources sources = cenv.getPropertySources();

        StreamSupport.stream(sources.spliterator(), false)
                .filter(item -> !(item instanceof DecryptPropertySourceWrapper))
                .map(item -> makeDecryptable(item, beanFactory))
                .collect(Collectors.toList())
                .forEach(item -> sources.replace(item.getName(), item));
    }

    public static final String IGNORE_SOURCE_SPRING="org.springframework.boot.context.properties.source.ConfigurationPropertySourcesPropertySource";

    public<T> PropertySource<T> makeDecryptable(PropertySource<T> source,ConfigurableListableBeanFactory beanFactory){
        if(source.getClass().getName().equals(IGNORE_SOURCE_SPRING)){
            return source;
        }
        PropertySource<T> ret=new DecryptPropertySourceWrapper<>(source,decryptor);
        return ret;
    }
}
