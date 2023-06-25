package i2f.core.streaming.base.process;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.streaming.Streaming;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class UnionStreaming<E> extends AbsStreaming<E, E> {
    private Streaming<E> stream;

    public UnionStreaming(Streaming<E> stream) {
        this.stream = stream;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            List<E> ret = new LinkedList<E>();
            while (iterator.hasNext()) {
                E item = iterator.next();
                ret.add(item);
            }
            stream.collect(ret);
            return ret.iterator();
        });
    }
}
