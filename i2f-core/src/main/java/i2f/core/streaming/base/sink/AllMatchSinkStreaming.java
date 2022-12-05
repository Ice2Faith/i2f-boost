package i2f.core.streaming.base.sink;

import i2f.core.functional.common.IFilter;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class AllMatchSinkStreaming<E> extends AbsSinkStreaming<Boolean, E, E> {
    private IFilter<E> filter;

    public AllMatchSinkStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    protected Boolean sink(Iterator<E> iterator, ExecutorService pool) {
        while (iterator().hasNext()) {
            E item = iterator.next();
            if (!filter.test(item)) {
                return false;
            }
        }
        return true;
    }
}