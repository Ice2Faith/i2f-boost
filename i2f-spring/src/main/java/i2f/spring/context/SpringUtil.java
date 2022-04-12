package i2f.spring.context;

import i2f.spring.environment.EnvironmentUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 15:29
 * @desc
 */
@Component
public class SpringUtil implements
        ApplicationContextAware,
        BeanFactoryAware,
        EnvironmentAware,
        ResourceLoaderAware {

    private static volatile ApplicationContext applicationContext;
    private static volatile BeanFactory beanFactory;
    private static volatile Environment environment;
    private static volatile ResourceLoader resourceLoader;
    private static volatile BeanDefinitionRegistry beanDefinitionRegistry;

    private static volatile Map<String, Map<String,Object>> environmentMap=new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if(SpringUtil.applicationContext == null){
            SpringUtil.applicationContext = applicationContext;
            beanDefinitionRegistry=(BeanDefinitionRegistry)((ConfigurableApplicationContext)applicationContext).getBeanFactory();
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if(SpringUtil.beanFactory == null){
            SpringUtil.beanFactory = beanFactory;
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        if(SpringUtil.environment == null){
            SpringUtil.environment = environment;
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        if(SpringUtil.resourceLoader == null){
            SpringUtil.resourceLoader = resourceLoader;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static BeanFactory getBeanFactory(){
        return beanFactory;
    }

    public static Environment getEnvironment(){
        return environment;
    }

    public static String getValue(String name){
        return getEnvironment().getProperty(name);
    }

    public static ResourceLoader getResourceLoader(){
        return resourceLoader;
    }

    public static BeanDefinitionRegistry getBeanDefinitionRegistry(){
        return beanDefinitionRegistry;
    }

    public static Map<String,Map<String,Object>> getEnvironmentMap(boolean forceNew){
        if(forceNew || environmentMap.size()==0){
            Map<String,Map<String,Object>> map= EnvironmentUtil.getEnvironmentProperties(getEnvironment());
            environmentMap=map;
        }
        return environmentMap;
    }

    public static Map<String,Object> getEnvironmentPropertiesWithPrefix(boolean keepPrefix,String prefix){
        return EnvironmentUtil.getPropertiesWithPrefix(getEnvironmentMap(false),keepPrefix,prefix);
    }

    public static Resource getResource(String match){
        return resourceLoader.getResource(match);
    }
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name,clazz);
    }

    public static AutowireCapableBeanFactory getAutowireCapableBeanFactory(){
        return getApplicationContext().getAutowireCapableBeanFactory();
    }

    public static void makeAutowireSupport(Object existBean,int autowireMode,boolean dependencyCheck){
        getAutowireCapableBeanFactory().autowireBeanProperties(existBean,autowireMode,dependencyCheck);
    }

    public static void makeAutowireSupport(Object existBean){
        getAutowireCapableBeanFactory().autowireBeanProperties(existBean,AutowireCapableBeanFactory.AUTOWIRE_BY_NAME,false);
    }

    public static <T> T registerBean(String name,Class<T> clazz,Object ... args){
        return registerBean((ConfigurableApplicationContext) applicationContext,name,clazz,args);
    }

    public static void registerSingletonBean(String name,Object bean){
        registerSingletonBean((DefaultListableBeanFactory)beanFactory,name,bean);
    }

    public static <T> T removeBean(String name){
        return removeBean((ConfigurableApplicationContext) applicationContext,name);
    }

    public static <T> T removeBean(ConfigurableApplicationContext applicationContext,String name){
        T ret=null;
        if(applicationContext.containsBean(name)){
            ret=(T)applicationContext.getBean(name);
            BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
            beanFactory.removeBeanDefinition(name);
        }
        return ret;
    }

    public static void registerSingletonBean(DefaultListableBeanFactory beanFactory,String name,Object bean){
        beanFactory.registerSingleton(name,bean);
    }

    public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz,
                                     Object... args) {
        if(applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("repeat bean register which name is :" + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }


}

