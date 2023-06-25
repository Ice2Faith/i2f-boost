package i2f.core.zplugin.validate;

import i2f.core.lang.proxy.IInvokable;
import i2f.core.lang.proxy.impl.BasicProxyHandler;
import i2f.core.lang.proxy.impl.IMethodAccessInvokable;
import i2f.core.zplugin.validate.core.ValidateProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/3/28 8:57
 * @desc
 */
public class ValidateProxyHandler extends BasicProxyHandler {
    @Override
    public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
        if(!(invokable instanceof IMethodAccessInvokable)){
            return null;
        }
        IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
        Method method= invoker.method();
        Parameter[] parameters= method.getParameters();
        for(int i=0;i< parameters.length;i++){
            ValidateProvider.valid(parameters[i],args[i]);
        }
        return null;
    }

    @Override
    public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        if(!(invokable instanceof IMethodAccessInvokable)){
            return null;
        }
        IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
        Method method= invoker.method();
        ValidateProvider.valid(method,retVal);
        return retVal;
    }

}
