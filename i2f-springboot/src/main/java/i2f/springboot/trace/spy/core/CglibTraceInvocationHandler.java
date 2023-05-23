package i2f.springboot.trace.spy.core;

import i2f.springboot.trace.spy.data.InvokeTraceMeta;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/5/23 15:29
 * @desc
 */
public class CglibTraceInvocationHandler implements InvocationHandler {
    private Object target;
    private Consumer<InvokeTraceMeta> consumer;

    public CglibTraceInvocationHandler(Object target, Consumer<InvokeTraceMeta> consumer) {
        this.target = target;
        this.consumer = consumer;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(true);
        }
        return InvokeTrace.trace(() -> {
                    return method.invoke(target, args);
                },
                consumer,
                target.getClass(),
                method, args);
    }
}
