package i2f.core.streaming.api.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class ProcessStreaming<E, R> extends AbsStreaming<R, E> {
    private IProcessStreaming<R, E> processor;

    public ProcessStreaming(IProcessStreaming<R, E> processor) {
        this.processor = processor;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            return processor.apply(iterator, pool);
        });
    }

    @Override
    public void create() {
        this.processor.create();
    }

    @Override
    public void destroy() {
        this.processor.destroy();
    }
}
