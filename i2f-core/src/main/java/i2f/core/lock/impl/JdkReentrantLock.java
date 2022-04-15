package i2f.core.lock.impl;

import i2f.core.lock.INotifyLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ltb
 * @date 2022/4/15 8:40
 * @desc
 */
public class JdkReentrantLock implements INotifyLock {
    protected ReentrantLock lock;
    protected Condition cond;

    public JdkReentrantLock(){
        lock=new ReentrantLock();
        cond=lock.newCondition();
    }

    public JdkReentrantLock(ReentrantLock lock){
        this.lock=lock;
        this.cond=this.lock.newCondition();
    }

    public JdkReentrantLock(ReentrantLock lock,Condition cond){
        this.lock=lock;
        this.cond=cond;
    }

    @Override
    public void lock() throws Throwable {
        lock.lock();
    }

    @Override
    public void unlock() throws Throwable {
        lock.unlock();
    }

    @Override
    public void signal() throws Throwable{
        cond.signal();
    }

    @Override
    public void signalAll() throws Throwable {
        cond.signalAll();
    }

    @Override
    public void await() throws Throwable{
        cond.await();
    }
}
