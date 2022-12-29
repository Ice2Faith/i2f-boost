package i2f.core.streaming.base.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.base.AbsStreaming;
import i2f.core.streaming.Streaming;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class ExcludeStreaming<E> extends AbsStreaming<E, E> {
    private Streaming<E> stream;

    public ExcludeStreaming(Streaming<E> stream) {
        this.stream = stream;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            Set<E> exSet = new LinkedHashSet<>();
            stream.collect(exSet);
            List<E> ret = new LinkedList<E>();
            while (iterator.hasNext()) {
                E item = iterator.next();
                if (!exSet.contains(item)) {
                    ret.add(item);
                }
            }
            return ret.iterator();
        });
    }
}
