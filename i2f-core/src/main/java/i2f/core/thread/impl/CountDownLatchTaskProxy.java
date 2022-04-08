package i2f.core.thread.impl;

import i2f.core.thread.ITaskProxy;

import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/3/26 13:11
 * @desc
 */
public class CountDownLatchTaskProxy implements ITaskProxy {
    protected CountDownLatch latch;
    protected ITaskProxy proxy;
    protected Object params;
    public CountDownLatchTaskProxy(ITaskProxy proxy,CountDownLatch latch,Object ... params){
        this.proxy=proxy;
        this.latch=latch;
        this.params=params;
    }
    @Override
    public <T> T task(Object... args) throws Throwable {
        try{
            return proxy.task(args);
        }finally {
            latch.countDown();
        }
    }
}
