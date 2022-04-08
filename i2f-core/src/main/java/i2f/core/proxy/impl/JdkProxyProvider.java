package i2f.core.proxy.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.proxy.IProxyHandler;
import i2f.core.proxy.IProxyProvider;
import i2f.core.resource.ResourceUtil;

import java.lang.reflect.Proxy;

/**
 * @author ltb
 * @date 2022/3/25 20:24
 * @desc
 */
@Author("i2f")
@Remark("base on jdk dynamic proxy's proxy provider")
public class JdkProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        Class<?> clazz=obj.getClass();
        Class<?>[] interfaces=clazz.getInterfaces();
        return (T)Proxy.newProxyInstance(ResourceUtil.getLoader(),interfaces,new JdkProxyHandlerAdapter<>(obj,handler));
    }
    public<T> T proxyNative(T obj,IProxyHandler handler){
        return proxy(obj,handler);
    }
}
