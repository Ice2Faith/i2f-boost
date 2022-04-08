package i2f.core.thread.impl;

import i2f.core.thread.ITaskProxy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ltb
 * @date 2022/3/26 12:59
 * @desc
 */
public class ReentrantLockTaskProxy implements ITaskProxy {
    protected ReentrantLock lock;
    protected ITaskProxy proxy;
    protected Object[] params;
    protected Condition cond;
    public ReentrantLockTaskProxy(ITaskProxy proxy,ReentrantLock lock,Condition cond,Object ... params){
        this.proxy=proxy;
        this.lock=lock;
        this.cond=cond;
        this.params=params;
    }
    @Override
    public <T> T task(Object... args) throws Throwable{
        lock.lock();
        try{
            T ret=proxy.task(args);
            onTaskEnd(lock,ret,proxy,args);
            return ret;
        }finally {
            lock.unlock();
        }
    }

    protected  <T> void onTaskEnd(ReentrantLock lock, T ret, ITaskProxy proxy, Object... args) {
        if(cond!=null){
            cond.signalAll();
        }
    }
}
