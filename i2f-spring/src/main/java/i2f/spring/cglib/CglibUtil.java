package i2f.spring.cglib;

import i2f.core.lang.proxy.IProxyHandler;

/**
 * @author ltb
 * @date 2022/3/26 19:50
 * @desc spring内嵌的cglib
 * 使用上与cglib无差别
 * 但是，如果同时使用这两者，可能导致重复定义类
 * 原因是cglib使用缓存的方式生成代理类
 * 所以，当spring内嵌的cglib生成的类和cglib生成的类重复的时候，就会出现重复定义的类
 * 因为，缓存的类名一致
 */
public class CglibUtil {
    public static<T> T proxy(Class<T> clazz, IProxyHandler handler){
        return new CglibProxyProvider().proxyNative(clazz,handler);
    }
}
