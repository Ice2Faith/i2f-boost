package i2f.core.thread;

import i2f.core.thread.impl.CountDownLatchTaskProxy;
import i2f.core.thread.impl.ReentrantLockTaskProxy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ltb
 * @date 2022/3/26 12:54
 * @desc
 */
public class LockUtil {
    public static CountDownLatch countDownLatch(int count){
        return new CountDownLatch(count);
    }
    public static ReentrantLock reentrantLock(){
        return new ReentrantLock();
    }
    public static ReadWriteLock readWriteLock(){
        return new ReentrantReadWriteLock();
    }
    public static ITaskProxy reentrantLockTask(ITaskProxy proxy, ReentrantLock lock, Condition cond, Object ... params){
        return new ReentrantLockTaskProxy(proxy,lock,cond,params);
    }
    public static ITaskProxy reentrantLockTask(ITaskProxy proxy, ReentrantLock lock, Object ... params){
        return new ReentrantLockTaskProxy(proxy,lock,null,params);
    }
    public static ITaskProxy countDownLatchTask(ITaskProxy proxy,CountDownLatch latch,Object ... params){
        return new CountDownLatchTaskProxy(proxy,latch,params);
    }
}
