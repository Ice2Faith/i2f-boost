package i2f.core.thread.impl;

import i2f.core.thread.ITaskProxy;

import java.util.concurrent.Callable;

/**
 * @author ltb
 * @date 2022/3/26 13:29
 * @desc
 */
public class CallableTaskProxyAdapter<T> implements Callable<T> {
    protected ITaskProxy proxy;
    protected Object[] params;
    protected Throwable ex;
    public CallableTaskProxyAdapter(ITaskProxy proxy,Object ... params){
        this.proxy=proxy;
        this.params=params;
    }
    @Override
    public T call() throws Exception {
        try{
            return proxy.task(params);
        }catch(Throwable e){
            this.ex=e;
            throw new Exception(e.getMessage(),e);
        }
    }
}
