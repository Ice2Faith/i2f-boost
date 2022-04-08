package i2f.extension.proxy.cglib;

import i2f.core.proxy.IProxyHandler;

/**
 * @author ltb
 * @date 2022/3/26 19:50
 * @desc
 */
public class CglibUtil {
    public static<T> T proxy(Class<T> clazz, IProxyHandler handler){
        return new CglibProxyProvider().proxyNative(clazz,handler);
    }
}
