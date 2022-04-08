package i2f.extension.aspectj.core;

import i2f.core.proxy.IInvokable;
import i2f.core.proxy.IProxyHandler;
import i2f.extension.aspectj.impl.AspectjInvoker;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/3/26 19:35
 * @desc
 */
public class AspectjProxy {
    private IProxyHandler handler;
    public AspectjProxy(IProxyHandler handler){
        this.handler=handler;
    }
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature ms=(MethodSignature) pjp.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();
        Parameter[] params=method.getParameters();
        Object[] args=pjp.getArgs();

        IInvokable invokable=new AspectjInvoker(pjp);
        Object context=handler.initContext();
        Object stopObj=handler.before(context,null,invokable,args);
        if(stopObj!=null){
            return (T)stopObj;
        }

        try{
            Object ret=pjp.proceed(args);
            ret=handler.after(context,null,invokable,ret,args);
            return (T)ret;
        }catch(Throwable e){
            throw handler.except(context,null,invokable,e,args);
        }
    }
}
