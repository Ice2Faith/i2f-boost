package i2f.container.sync.adapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2024/4/18 17:48
 * @desc
 */
public class SyncProxyAdapter implements InvocationHandler {
    private Object target;
    private Lock lock = new ReentrantLock();

    public static <T, E extends T> T proxy(Class<T> clazz, E target) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                new SyncProxyAdapter(target));
    }

    public static <T, E extends T> T proxy(Class<T> clazz, E target, Lock lock) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                new SyncProxyAdapter(target, lock));
    }

    public SyncProxyAdapter(Object target) {
        this.target = target;
    }

    public SyncProxyAdapter(Object target, Lock lock) {
        this.target = target;
        this.lock = lock;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        lock.lock();
        try {
            return method.invoke(target, args);
        } finally {
            lock.unlock();
        }
    }
}
