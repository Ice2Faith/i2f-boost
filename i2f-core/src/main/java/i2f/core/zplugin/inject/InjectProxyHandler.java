package i2f.core.zplugin.inject;

import i2f.core.proxy.IInvokable;
import i2f.core.proxy.impl.BasicProxyHandler;
import i2f.core.proxy.impl.IMethodAccessInvokable;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.zplugin.inject.annotations.Injects;
import i2f.core.zplugin.inject.core.InjectProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/5/9 15:30
 * @desc
 */
public class InjectProxyHandler extends BasicProxyHandler {
    protected InjectProvider provider;
    public InjectProxyHandler(InjectProvider provider){
        this.provider=provider;
    }
    @Override
    public Object before(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        if(!(invokable instanceof IMethodAccessInvokable)){
            return null;
        }
        IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
        Method method= invoker.method();
        Parameter[] params= method.getParameters();
        for(int i=0;i< params.length;i++){
            Parameter item = params[i];
            Object val=args[i];
            Injects ann= ReflectResolver.findAnnotation(item,Injects.class,false);
            if(ann!=null){
                provider.inject(val,ann);
            }
        }
        return null;
    }

    @Override
    public Object after(Object context, Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        if(!(invokable instanceof IMethodAccessInvokable)){
            return retVal;
        }
        IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
        Method method= invoker.method();
        Injects ann= ReflectResolver.findAnnotation(method,Injects.class,false);
        if(ann!=null){
            provider.inject(retVal,ann);
        }
        return retVal;
    }


}
