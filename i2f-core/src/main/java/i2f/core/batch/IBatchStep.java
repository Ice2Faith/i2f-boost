package i2f.core.batch;

import i2f.core.cycle.ILifeCycle;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/4/14 15:18
 * @desc
 */
public interface IBatchStep<T,E> extends ILifeCycle {
    Collection<E> process(Collection<T> col);
}
