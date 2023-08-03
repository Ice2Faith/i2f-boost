package i2f.springboot.log.impl;

import i2f.std.invoke.IProxyInvokable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/8/3 15:09
 * @desc
 */
public class AspectProxyInvokable implements IProxyInvokable {
    private ProceedingJoinPoint pjp;

    public AspectProxyInvokable(ProceedingJoinPoint pjp) {
        this.pjp = pjp;
    }

    @Override
    public Object[] getArgs() {
        return pjp.getArgs();
    }

    @Override
    public Object invoke(Object[] args) throws Throwable {
        return pjp.proceed(args);
    }

    @Override
    public Method getMethod() {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        return method;
    }

    @Override
    public Object invoke() throws Throwable {
        return pjp.proceed();
    }
}
