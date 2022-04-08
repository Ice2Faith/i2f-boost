package i2f.core.thread.std;

import i2f.core.annotations.remark.Author;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Author("i2f")
public class FixedThreadPool {
    public static volatile int POOL_SIZE=10;
    protected static volatile ExecutorService pool;
    public static ExecutorService getPool(){
        if(pool==null){
            synchronized (FixedThreadPool.class){
                if(pool==null){
                    pool= Executors.newFixedThreadPool(POOL_SIZE);
                }
            }
        }
        return pool;
    }
    public static Future<?> submit(Runnable runnable){
        return getPool().submit(runnable);
    }
    public static<T> Future<T> submit(Callable<T> callable){
        return getPool().submit(callable);
    }
}
