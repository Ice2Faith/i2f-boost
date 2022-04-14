package i2f.core.batch;

import i2f.core.cycle.ILifeCycle;

/**
 * @author ltb
 * @date 2022/4/14 11:39
 * @desc
 */
public interface IBatchProcessor<T,E> extends ILifeCycle {
    default boolean beforeFilter(T val){
        return true;
    }
    E process(T val);
    default boolean afterFilter(E val){
        return true;
    }
}
