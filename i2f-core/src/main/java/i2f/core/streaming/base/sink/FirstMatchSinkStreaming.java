package i2f.core.streaming.base.sink;

import i2f.core.lang.functional.common.IFilter;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class FirstMatchSinkStreaming<E> extends AbsSinkStreaming<E, E, E> {

    private IFilter<E> filter;

    public FirstMatchSinkStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    protected E sink(Iterator<E> iterator, ExecutorService pool) {
        E ret = null;
        while (iterator().hasNext()) {
            E item = iterator.next();
            if (filter.test(item)) {
                ret = item;
                break;
            }
        }
        return ret;
    }
}
