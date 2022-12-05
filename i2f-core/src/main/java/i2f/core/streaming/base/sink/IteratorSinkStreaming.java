package i2f.core.streaming.base.sink;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class IteratorSinkStreaming<E> extends AbsSinkStreaming<Iterator<E>, E, E> {

    public IteratorSinkStreaming() {

    }

    @Override
    protected Iterator<E> sink(Iterator<E> iterator, ExecutorService pool) {
        return iterator;
    }
}
