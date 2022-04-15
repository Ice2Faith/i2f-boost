package i2f.extension.zookeeper.lock;

import i2f.core.lock.ILock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/15 8:37
 * @desc
 */
public class ZookeeperInterMutexLock implements ILock {
    protected InterProcessMutex mutex;
    public ZookeeperInterMutexLock(InterProcessMutex mutex){
        this.mutex=mutex;
    }

    @Override
    public void lock() throws Throwable {
        mutex.acquire();
    }

    @Override
    public void unlock() throws Throwable {
        mutex.release();
    }

    public void lock(long time, TimeUnit timeUnit) throws Throwable{
        mutex.acquire(time,timeUnit);
    }
}
