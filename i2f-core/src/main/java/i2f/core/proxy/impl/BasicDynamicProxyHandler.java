package i2f.core.proxy.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.proxy.IInvokable;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/25 21:11
 * @desc
 */
@Author("i2f")
@Remark("provide default implement for simple use it.")
public abstract class BasicDynamicProxyHandler extends BasicProxyHandler {
    @Override
    public Object initContext() {
        return null;
    }

    @Override
    public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
        MethodInvoker invoker=(MethodInvoker)invokable;
        Method method= invoker.method();
        return resolve(context,ivkObj,method,args);
    }

    @Override
    public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        return retVal;
    }

    @Override
    public Throwable except(Object context,Object ivkObj, IInvokable invokable, Throwable ex, Object... args) {
        return ex;
    }

    @Override
    public void onFinally(Object context, Object ivkObj, IInvokable invokable, Object... args) {

    }

    public abstract Object resolve(Object context,Object ivkObj,Method method,Object ... args);
}
