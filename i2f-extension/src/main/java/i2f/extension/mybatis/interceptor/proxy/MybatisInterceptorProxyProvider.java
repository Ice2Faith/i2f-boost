package i2f.extension.mybatis.interceptor.proxy;

import i2f.core.lang.proxy.IProxyHandler;
import i2f.core.lang.proxy.IProxyProvider;
import i2f.extension.mybatis.interceptor.proxy.core.MybatisInterceptorProxy;

/**
 * @author ltb
 * @date 2022/4/4 15:51
 * @desc
 */
public class MybatisInterceptorProxyProvider implements IProxyProvider {
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        return (T)new MybatisInterceptorProxy(handler);
    }
    public MybatisInterceptorProxy proxyNative(IProxyHandler handler){
        return proxy(null,handler);
    }
}
