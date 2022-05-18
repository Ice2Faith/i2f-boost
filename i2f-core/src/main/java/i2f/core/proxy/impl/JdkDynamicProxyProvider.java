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
@Remark("base on jdk dynamic proxy's proxy interfaces provider")
public class JdkDynamicProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        Class<?>[] interfaces=new Class<?>[]{(Class<?>)obj};
        return (T)Proxy.newProxyInstance(ResourceUtil.getLoader(),interfaces,new JdkDynamicProxyHandlerAdapter(handler));
    }
    public<T> T proxyNative(Class<T> interfaces,IProxyHandler handler){
        return proxy(interfaces,handler);
    }
}
