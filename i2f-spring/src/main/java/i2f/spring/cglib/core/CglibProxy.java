package i2f.spring.cglib.core;

import i2f.core.proxy.IProxyHandler;
import i2f.spring.cglib.IEnhancerSetting;
import i2f.spring.cglib.impl.CglibProxyHandlerAdapter;
import i2f.spring.cglib.impl.NoneEnhancerSetting;
import org.springframework.cglib.proxy.Enhancer;


public class CglibProxy {
    private IEnhancerSetting setting;

    public CglibProxy() {
        this.setting = new NoneEnhancerSetting();
    }

    public CglibProxy(IEnhancerSetting setting) {
        this.setting = setting;
    }

    public IEnhancerSetting setSetting(IEnhancerSetting setting) {
        IEnhancerSetting old = this.setting;
        this.setting = setting;
        return old;
    }

    public <T> T getProxy(Class<T> srcClass, CglibProxyHandlerAdapter invoker) {
        Enhancer enhancer = new Enhancer();
        if (setting != null) {
            setting.set(enhancer);
        }
        enhancer.setUseFactory(true);
        enhancer.setSuperclass(srcClass);
        enhancer.setCallback(invoker);
        return (T) enhancer.create();
    }

    public <T> T getProxy(Class<T> srcClass, IProxyHandler handler) {
        return getProxy(srcClass, new CglibProxyHandlerAdapter(handler));
    }
}
