package i2f.springcloud.spring.listener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.Environment;

/**
 * @author ltb
 * @date 2022/6/22 17:28
 * @desc
 */
public class DynamicConfigRefreshListenerAdapter implements ApplicationContextAware, EnvironmentAware, BeanFactoryAware, SmartApplicationListener {
    protected BeanFactory beanFactory;
    protected ApplicationContext applicationContext;
    protected Environment environment;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType)
                || ContextClosedEvent.class.isAssignableFrom(eventType)
                || RefreshEvent.class.isAssignableFrom(eventType)
                || RefreshScopeRefreshedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextClosedEvent){
            onClose((ContextClosedEvent)event);
            return;
        }
        if(event instanceof ApplicationReadyEvent){
            onReady((ApplicationReadyEvent)event);
        }
        onRefresh(event);
    }

    // 容器销毁
    public void onClose(ContextClosedEvent event){

    }

    // 初始化完成时
    public void onReady(ApplicationReadyEvent event){

    }

    // 环境刷新时，需要实现幂等
    public void onRefresh(ApplicationEvent event){

    }
}
