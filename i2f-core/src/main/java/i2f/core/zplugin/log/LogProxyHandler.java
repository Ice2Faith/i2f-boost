package i2f.core.zplugin.log;

import i2f.core.lang.proxy.IInvokable;
import i2f.core.lang.proxy.impl.BasicProxyHandler;
import i2f.core.lang.proxy.impl.IMethodAccessInvokable;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.zplugin.log.annotations.Log;
import i2f.core.zplugin.log.data.LogData;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/28 16:44
 * @desc
 */
public class LogProxyHandler extends BasicProxyHandler {
    private ILogWriter writer;
    public LogProxyHandler(ILogWriter writer){
        this.writer=writer;
    }

    @Override
    public Object initContext() {
        return System.currentTimeMillis();
    }

    @Override
    public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
        Log log=null;
        Method method=null;
        if(invokable instanceof IMethodAccessInvokable){
            IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
            method= invoker.method();
            log=ReflectResolver.findElementAnnotation(method,Log.class,true,false,false);
        }
        if(log!=null && log.value() && log.before()) {
            writer.write(LogData.before(log, method, args));
        }
        return null;
    }

    @Override
    public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        Log log=null;
        Method method=null;
        if(invokable instanceof IMethodAccessInvokable){
            IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
            method= invoker.method();
            log=ReflectResolver.findElementAnnotation(method,Log.class,true,false,false);
        }
        if(log!=null && log.value() && log.after()){
            writer.write(LogData.after(log,method,retVal,args).withUseTime(System.currentTimeMillis()-((long)context)));
        }
        return retVal;
    }

    @Override
    public Throwable except(Object context,Object ivkObj, IInvokable invokable, Throwable ex, Object... args) {
        Log log=null;
        Method method=null;
        if(invokable instanceof IMethodAccessInvokable){
            IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
            method= invoker.method();
            log=ReflectResolver.findElementAnnotation(method,Log.class,true,false,false);
        }
        if(log!=null && log.value() && log.except()){
            writer.write(LogData.except(log,method,ex,args).withUseTime(System.currentTimeMillis()-((long)context)));
        }
        return ex;
    }
}
