package i2f.spring.cglib;

import i2f.core.proxy.IProxyHandler;
import i2f.core.proxy.IProxyProvider;
import i2f.spring.cglib.core.CglibProxy;


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
