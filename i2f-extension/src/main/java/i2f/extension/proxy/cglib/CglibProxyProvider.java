package i2f.extension.proxy.cglib;

import i2f.core.lang.proxy.IProxyHandler;
import i2f.core.lang.proxy.IProxyProvider;
import i2f.extension.proxy.cglib.core.CglibProxy;

/**
 * @author ltb
 * @date 2022/3/26 17:50
 * @desc
 */
public class CglibProxyProvider implements IProxyProvider {
    private CglibProxy proxy;
    public CglibProxyProvider(){
        proxy = new CglibProxy();
    }
    public CglibProxyProvider(CglibProxy proxy){
        this.proxy=proxy;
    }
    public CglibProxyProvider(IEnhancerSetting setting){
        proxy=new CglibProxy(setting);
    }
    @Override
    public <T> T proxy(Object obj, IProxyHandler handler) {
        return proxy.getProxy((Class<T>)obj,handler);
    }

    public<T> T proxyNative(Class<T> clazz,IProxyHandler handler){
        return proxy(clazz,handler);
    }
}
