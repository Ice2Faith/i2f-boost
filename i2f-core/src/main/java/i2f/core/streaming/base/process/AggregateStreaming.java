package i2f.core.streaming.base.process;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.lang.functional.jvf.BiConsumer;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class AggregateStreaming<E, R> extends AbsStreaming<R, E> {
    private BiConsumer<Iterator<E>, Collection<R>> mapper;

    public AggregateStreaming(BiConsumer<Iterator<E>, Collection<R>> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            List<R> ret = new LinkedList<R>();
            mapper.accept(iterator, ret);
            return ret.iterator();
        });
    }
}
