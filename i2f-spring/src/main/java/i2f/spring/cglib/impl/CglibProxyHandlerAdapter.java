package i2f.spring.cglib.impl;

import i2f.core.proxy.IInvokable;
import i2f.core.proxy.IProxyHandler;
import i2f.core.proxy.impl.MethodInvoker;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/26 17:57
 * @desc
 */
public class CglibProxyHandlerAdapter implements MethodInterceptor {
    private IProxyHandler handler;

    public CglibProxyHandlerAdapter(IProxyHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        IInvokable invokable=new MethodInvoker(method);
        Object context=handler.initContext();
        Object stopVal = handler.before(context,obj, invokable, args);
        if (stopVal != null) {
            return stopVal;
        }

        try{
            Object ret = methodProxy.invokeSuper(obj, args);
            ret = handler.after(context,obj,invokable,ret, args);
            return ret;
        }catch(Throwable e){
            throw handler.except(context,obj,invokable,e,args);
        }finally {
            handler.onFinally(context,obj,invokable,args);
        }
    }
}
