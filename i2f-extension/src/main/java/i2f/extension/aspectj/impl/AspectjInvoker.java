package i2f.extension.aspectj.impl;

import i2f.core.proxy.impl.IMethodAccessInvokable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/26 19:27
 * @desc
 */
public class AspectjInvoker implements IMethodAccessInvokable {
    protected ProceedingJoinPoint pjp;
    protected Method method;
    public AspectjInvoker(ProceedingJoinPoint pjp){
        this.pjp=pjp;
        MethodSignature ms=(MethodSignature) pjp.getSignature();
        method=ms.getMethod();
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        return pjp.proceed(args);
    }

    @Override
    public Method method() {
        return method;
    }
}
