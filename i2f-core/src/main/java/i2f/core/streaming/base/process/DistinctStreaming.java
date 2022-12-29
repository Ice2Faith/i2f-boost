package i2f.core.streaming.base.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class DistinctStreaming<E> extends AbsStreaming<E, E> {

    public DistinctStreaming() {

    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            Set<E> ret = new ConcurrentSkipListSet<>();
            parallelizeProcess(iterator, pool, (item, collector) -> {
                ret.add(item);
            });
            return ret.iterator();
        });
    }
}
