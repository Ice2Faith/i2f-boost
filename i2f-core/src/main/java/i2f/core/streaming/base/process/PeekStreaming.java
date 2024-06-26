package i2f.core.streaming.base.process;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.lang.functional.jvf.Consumer;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class PeekStreaming<E> extends AbsStreaming<E, E> {
    private Consumer<E> consumer;

    public PeekStreaming(Consumer<E> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            return parallelizeProcess(iterator, pool, (item, collector) -> {
                collector.add(item);
                consumer.accept(item);
            });
        });
    }
}
