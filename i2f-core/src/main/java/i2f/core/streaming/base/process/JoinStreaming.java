package i2f.core.streaming.base.process;

import i2f.core.functional.jvf.BiFunction;
import i2f.core.functional.jvf.BiPredicate;
import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;
import i2f.core.streaming.Streaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class JoinStreaming<R, E, T> extends AbsStreaming<R, E> {
    private Streaming<T> stream;
    private BiPredicate<E, T> joiner;
    private BiFunction<R, E, T> processor;

    public JoinStreaming(Streaming<T> stream, BiPredicate<E, T> joiner, BiFunction<R, E, T> processor) {
        this.stream = stream;
        this.joiner = joiner;
        this.processor = processor;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            List<R> ret = new LinkedList<R>();
            List<T> list = new LinkedList<T>();
            stream.collect(list);
            while (iterator.hasNext()) {
                E item = iterator.next();
                for (T titem : list) {
                    if (joiner.test(item, titem)) {
                        R next = processor.get(item, titem);
                        ret.add(next);
                    }
                }
            }
            stream.collect(ret);
            return ret.iterator();
        });
    }
}
