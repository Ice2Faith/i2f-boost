package i2f.core.proxy.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.proxy.IInvokable;
import i2f.core.proxy.IProxyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/25 20:33
 * @desc
 */
@Author("i2f")
@Remark("adapt jdk dynamic proxy interface")
public class JdkDynamicProxyHandlerAdapter implements InvocationHandler {
    private IProxyHandler handler;
    public JdkDynamicProxyHandlerAdapter(IProxyHandler handler){
        this.handler=handler;
    }

    @Override
    final public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        IInvokable invokable=new MethodInvoker(method);
        Object context=handler.initContext();
        try{
            Object retVal=handler.before(context,proxy,invokable,args);
            retVal=handler.after(context,proxy,invokable,retVal,args);
            return retVal;
        }catch(Throwable e){
            throw handler.except(context,proxy,invokable,e,args);
        }finally {
            handler.onFinally(context,proxy,invokable,args);
        }
    }
}
