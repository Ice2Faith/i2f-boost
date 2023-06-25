package i2f.core.streaming.base.process;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.lang.functional.common.IFilter;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class OrFilterStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E>[] filters;

    public OrFilterStreaming(IFilter<E>... filters) {
        this.filters = filters;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            if (filters == null || filters.length == 0) {
                return iterator;
            }
            return parallelizeProcess(iterator, pool, (item, collector) -> {
                for (IFilter<E> filter : filters) {
                    if (filter.test(item)) {
                        collector.add(item);
                        break;
                    }
                }
            });
        });
    }
}
