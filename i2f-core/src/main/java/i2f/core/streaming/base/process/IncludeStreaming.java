package i2f.core.streaming.base.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;
import i2f.core.streaming.Streaming;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class IncludeStreaming<E> extends AbsStreaming<E, E> {
    private Streaming<E> stream;

    public IncludeStreaming(Streaming<E> stream) {
        this.stream = stream;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            Set<E> ret = new LinkedHashSet<E>();
            while (iterator.hasNext()) {
                E item = iterator.next();
                ret.add(item);
            }
            stream.collect(ret);
            return ret.iterator();
        });
    }
}
