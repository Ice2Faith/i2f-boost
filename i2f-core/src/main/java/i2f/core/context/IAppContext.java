package i2f.core.context;

import java.util.Map;

public interface IAppContext {
    void registryBean(String name,Object bean);
    <T> T getBean(String name);
    <T> T getBean(Class<T> clazz);
    <T> Map<String,T> getBeans(Class<T> clazz);
    boolean existBean(String name);
    boolean existBean(Class<?> clazz);
    void removeBean(String name);

    Object getProperty(String name);
    Map<String,Object> getProperties();
    boolean existProperty(String name);
    void removeProperty(String name);
}
