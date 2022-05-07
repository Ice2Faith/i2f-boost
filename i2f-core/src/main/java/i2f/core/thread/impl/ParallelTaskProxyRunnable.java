package i2f.core.thread.impl;

import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/3/26 13:11
 * @desc
 */
public class ParallelTaskProxyRunnable implements Runnable{
    protected CountDownLatch latch;
    protected Runnable proxy;
    public ParallelTaskProxyRunnable(Runnable proxy, CountDownLatch latch){
        this.proxy=proxy;
        this.latch=latch;

    }

    @Override
    public void run() {
        try{
            proxy.run();
        }finally {
            latch.countDown();
        }
    }
}
