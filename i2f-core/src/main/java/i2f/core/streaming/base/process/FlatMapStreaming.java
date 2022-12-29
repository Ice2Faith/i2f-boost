package i2f.core.streaming.base.process;

import i2f.core.functional.jvf.BiConsumer;
import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class FlatMapStreaming<E, R> extends AbsStreaming<R, E> {
    private BiConsumer<E, Collection<R>> mapper;

    public FlatMapStreaming(BiConsumer<E, Collection<R>> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            return parallelizeProcess(iterator, pool, (item, collector) -> {
                mapper.accept(item, collector);
            });
        });
    }
}
