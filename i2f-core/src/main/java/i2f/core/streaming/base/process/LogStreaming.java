package i2f.core.streaming.base.process;

import i2f.core.functional.common.IExecutor;
import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class LogStreaming<E, T> extends AbsStreaming<E, E> {
    private IExecutor<Object[]> executor;
    private Object[] args;

    public LogStreaming(IExecutor<Object[]> executor, Object... args) {
        this.executor = executor;
        this.args = args;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            executor.accept(args);
            return iterator;
        });
    }
}
