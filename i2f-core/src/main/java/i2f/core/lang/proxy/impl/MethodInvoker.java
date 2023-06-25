package i2f.core.lang.proxy.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/25 20:39
 * @desc
 */
@Author("i2f")
@Remark("adapt reflect method as invokable")
public class MethodInvoker implements IMethodAccessInvokable {
    private Method method;
    public MethodInvoker(Method method){
        this.method=method;
    }
    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        return method.invoke(ivkObj, args);
    }

    @Override
    public Method method() {
        return method;
    }
}
