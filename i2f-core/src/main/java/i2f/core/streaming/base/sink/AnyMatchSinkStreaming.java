package i2f.core.streaming.base.sink;

import i2f.core.lang.functional.common.IFilter;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class AnyMatchSinkStreaming<E> extends AbsSinkStreaming<Boolean, E, E> {
    private IFilter<E> filter;

    public AnyMatchSinkStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    protected Boolean sink(Iterator<E> iterator, ExecutorService pool) {
        while (iterator().hasNext()) {
            E item = iterator.next();
            if (filter.test(item)) {
                return true;
            }
        }
        return false;
    }
}
