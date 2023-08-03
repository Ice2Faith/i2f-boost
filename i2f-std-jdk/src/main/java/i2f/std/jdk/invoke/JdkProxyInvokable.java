package i2f.std.jdk.invoke;

import i2f.std.invoke.IProxyInvokable;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/8/3 15:12
 * @desc
 */
public class JdkProxyInvokable implements IProxyInvokable {
    private Method method;
    private Object target;
    private Object[] args=new Object[0];

    public JdkProxyInvokable(Method method, Object target, Object[] args) {
        this.method = method;
        this.target = target;
        this.args = args;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public Object invoke(Object[] args) throws Throwable {
        return method.invoke(target,args);
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object invoke() throws Throwable {
        return method.invoke(target,args);
    }
}
