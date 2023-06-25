package i2f.extension.aspectj;

import i2f.core.lang.proxy.IProxyHandler;
import i2f.core.zplugin.inject.InjectProxyHandler;
import i2f.core.zplugin.inject.core.InjectProvider;
import i2f.core.zplugin.validate.ValidateProxyHandler;
import i2f.extension.aspectj.core.AspectjProxy;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author ltb
 * @date 2022/3/26 19:42
 * @desc
 */
public class AspectjUtil {
    public static AspectjProxy proxy(IProxyHandler handler){
        return new AspectjProxyProvider().proxyNative(handler);
    }
    public static Object aop(ProceedingJoinPoint pjp, IProxyHandler handler) throws Throwable{
        AspectjProxy proxy=proxy(handler);
        return proxy.invoke(pjp);
    }
    public static Object validate(ProceedingJoinPoint pjp) throws Throwable{
        AspectjProxy proxy=proxy(new ValidateProxyHandler());
        return proxy.invoke(pjp);
    }
    public static Object inject(ProceedingJoinPoint pjp, InjectProvider provider) throws Throwable{
        AspectjProxy proxy=proxy(new InjectProxyHandler(provider));
        return proxy.invoke(pjp);
    }
    public static Object inject(ProceedingJoinPoint pjp) throws Throwable{
        AspectjProxy proxy=proxy(new InjectProxyHandler(InjectProvider.provider()));
        return proxy.invoke(pjp);
    }
}
