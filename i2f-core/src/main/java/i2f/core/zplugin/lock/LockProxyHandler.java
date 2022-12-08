package i2f.core.zplugin.lock;

import i2f.core.proxy.IInvokable;
import i2f.core.proxy.impl.BasicProxyHandler;
import i2f.core.proxy.impl.IMethodAccessInvokable;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.zplugin.lock.annotations.ReadLock;
import i2f.core.zplugin.lock.annotations.ReentrantLock;
import i2f.core.zplugin.lock.annotations.WriteLock;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockProxyHandler extends BasicProxyHandler {
    private volatile ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private volatile java.util.concurrent.locks.ReentrantLock reentrantLock = new java.util.concurrent.locks.ReentrantLock();

    @Override
    public Object before(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        if (!(invokable instanceof IMethodAccessInvokable)) {
            return null;
        }
        IMethodAccessInvokable invoker = (IMethodAccessInvokable) invokable;
        Method method = invoker.method();
        ReentrantLock lann = ReflectResolver.findAnnotation(method, ReentrantLock.class, false);
        if (lann != null) {
            reentrantLock.lock();
            return null;
        }

        WriteLock wann = ReflectResolver.findAnnotation(method, WriteLock.class, false);
        if (wann != null) {
            readWriteLock.writeLock().lock();
            return null;
        }

        ReadLock rann = ReflectResolver.findAnnotation(method, ReadLock.class, false);
        if (rann != null) {
            readWriteLock.readLock().lock();
            return null;
        }

        return null;
    }

    @Override
    public void onFinally(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        if (!(invokable instanceof IMethodAccessInvokable)) {
            return;
        }
        IMethodAccessInvokable invoker = (IMethodAccessInvokable) invokable;
        Method method = invoker.method();
        Parameter[] params = method.getParameters();
        ReentrantLock lann = ReflectResolver.findAnnotation(method, ReentrantLock.class, false);
        if (lann != null) {
            reentrantLock.unlock();
            return;
        }

        WriteLock wann = ReflectResolver.findAnnotation(method, WriteLock.class, false);
        if (wann != null) {
            readWriteLock.writeLock().unlock();
            return;
        }

        ReadLock rann = ReflectResolver.findAnnotation(method, ReadLock.class, false);
        if (rann != null) {
            readWriteLock.readLock().unlock();
            return;
        }
    }
}
