package i2f.core.thread.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/5/7 11:12
 * @desc
 */
public class ParallelTaskRunner{
    protected List<Runnable> runs=new ArrayList<>();
    protected CountDownLatch latch;
    protected ExecutorService pool;
    public ParallelTaskRunner(Runnable ... tasks){
        for(Runnable item : tasks){
            runs.add(item);
        }
    }
    public ParallelTaskRunner(ExecutorService pool,Runnable ... tasks){
        this.pool=pool;
        for(Runnable item : tasks){
            runs.add(item);
        }
    }
    public ParallelTaskRunner addTask(Runnable task){
        runs.add(task);
        return this;
    }
    public void parallel() throws InterruptedException {
        latch=new CountDownLatch(runs.size());
        for(Runnable item : runs){
            ParallelTaskProxyRunnable task=new ParallelTaskProxyRunnable(item,latch);
            if(pool!=null){
                pool.submit(task);
            }else{
                new Thread(task).start();
            }
        }
        latch.await();
    }
}
