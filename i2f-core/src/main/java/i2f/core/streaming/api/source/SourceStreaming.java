package i2f.core.streaming.api.source;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:02
 * @desc
 */
public class SourceStreaming<E> extends AbsStreaming<E, E> {
    private ISourceStreaming<E> source;

    public SourceStreaming(ISourceStreaming<E> source) {
        this.source = source;
    }

    @Override
    public Iterator<E> apply(Iterator<E> spaceHolder, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            return this.source.iterator();
        });
    }

    @Override
    public void create() {
        this.source.create();
    }

    @Override
    public void destroy() {
        this.source.destroy();
    }
}
