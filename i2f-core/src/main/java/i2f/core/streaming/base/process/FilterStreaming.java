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
public class FilterStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E> filter;

    public FilterStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            return parallelizeProcess(iterator, pool, (item, collector) -> {
                if (filter.test(item)) {
                    collector.add(item);
                }
            });
        });
    }
}
