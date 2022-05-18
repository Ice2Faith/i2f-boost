package i2f.core.proxy;

import i2f.core.proxy.impl.JdkDynamicProxyProvider;
import i2f.core.proxy.impl.JdkProxyProvider;

/**
 * @author ltb
 * @date 2022/3/26 19:52
 * @desc
 */
public class JdkProxyUtil {
    public static<T> T proxy(T srcObj, IProxyHandler handler){
        return new JdkProxyProvider().proxyNative(srcObj,handler);
    }
    public static<T> T proxy(Class<T> interfaces,IProxyHandler handler){
        return new JdkDynamicProxyProvider().proxyNative(interfaces, handler);
    }
}
