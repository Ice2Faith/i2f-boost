package i2f.extension.mybatis.interceptor.proxy.core;

import i2f.core.proxy.IProxyHandler;
import i2f.extension.mybatis.interceptor.proxy.impl.MybatisInvocationInvoker;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/4/4 15:49
 * @desc
 */
public class MybatisInterceptorProxy {
    private IProxyHandler handler;
    public MybatisInterceptorProxy(IProxyHandler handler){
        this.handler=handler;
    }
    public Object invoke(Invocation invocation) throws Throwable{
        Object ivkObj=invocation.getTarget();
        Method method=invocation.getMethod();
        Object[] args=invocation.getArgs();
        MybatisInvocationInvoker invoker=new MybatisInvocationInvoker(invocation);
        Object context=handler.initContext();
        Object stopObj=handler.before(context,ivkObj,invoker,args);
        if(stopObj!=null){
            return stopObj;
        }
        try{
            Object retVal=invocation.proceed();
            return handler.after(context,ivkObj,invoker,retVal,args);
        }catch(Throwable e){
            throw handler.except(context,ivkObj,invoker,e,args);
        }
    }
}
