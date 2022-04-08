package i2f.core.thread.impl;

import i2f.core.thread.ITaskProxy;

/**
 * @author ltb
 * @date 2022/3/26 13:26
 * @desc
 */
public class RunnableTaskProxyAdapter implements Runnable{
    protected ITaskProxy proxy;
    protected Object[] params;
    protected Throwable ex;
    public RunnableTaskProxyAdapter(ITaskProxy proxy,Object ... params){
        this.proxy=proxy;
        this.params=params;
    }
    @Override
    public void run() {
       try{
           proxy.task(params);
       }catch(Throwable e){
           ex=e;
           e.printStackTrace();
       }
    }
}
