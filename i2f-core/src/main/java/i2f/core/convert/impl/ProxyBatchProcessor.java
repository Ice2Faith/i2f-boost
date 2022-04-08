package i2f.core.convert.impl;

import i2f.core.convert.IBatchProcessor;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/3/27 18:52
 * @desc
 */
public abstract class ProxyBatchProcessor<T,E> implements IBatchProcessor<T> {
    protected E proxy;
    protected Object[] params;
    public ProxyBatchProcessor(E proxy,Object ... params){
        this.proxy=proxy;
        this.params=params;
    }
    @Override
    public void process(Collection<T> col) {
        doProcess(proxy,col,params);
    }
    public abstract void doProcess(E proxy,Collection<T> col,Object ... args);
}
