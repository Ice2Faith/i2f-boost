package i2f.extension.mybatis.interceptor.proxy.impl;

import i2f.core.lang.proxy.impl.IMethodAccessInvokable;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/4/4 15:40
 * @desc
 */
public class MybatisInvocationInvoker implements IMethodAccessInvokable {
    private Invocation invocation;
    public MybatisInvocationInvoker(Invocation invocation){
        this.invocation=invocation;
    }
    @Override
    public Method method() {
        return invocation.getMethod();
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        return invocation.proceed();
    }
}
