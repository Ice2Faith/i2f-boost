package i2f.core.streaming.base.sink;

import i2f.core.functional.jvf.BiFunction;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class ReduceSinkStreaming<E> extends AbsSinkStreaming<E, E, E> {
    public BiFunction<E, E, E> reducer;

    public ReduceSinkStreaming(BiFunction<E, E, E> reducer) {
        this.reducer = reducer;
    }

    @Override
    protected E sink(Iterator<E> iterator) {
        E ret = null;
        while (iterator.hasNext()) {
            E item = iterator.next();
            ret = reducer.get(ret, item);
        }
        return ret;
    }
}
