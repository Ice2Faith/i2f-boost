package i2f.core.streaming.base.sink;

import i2f.core.delegate.batch.BatchDelegator;
import i2f.core.delegate.batch.IBatchResolver;
import i2f.core.type.tuple.impl.Tuple2;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class BatchSinkStreaming<R, E> extends AbsSinkStreaming<List<Tuple2<R, Exception>>, E, E> {
    public int batchCount = -1;
    public IBatchResolver<R, E> resolver;
    public boolean throwEx = true;

    public BatchSinkStreaming(int batchCount, IBatchResolver<R, E> resolver) {
        this.batchCount = batchCount;
        this.resolver = resolver;
    }

    public BatchSinkStreaming(int batchCount, IBatchResolver<R, E> resolver, boolean throwEx) {
        this.batchCount = batchCount;
        this.resolver = resolver;
        this.throwEx = throwEx;
    }

    @Override
    protected List<Tuple2<R, Exception>> sink(Iterator<E> iterator, ExecutorService pool) {
        return BatchDelegator.batch(iterator, batchCount, resolver, throwEx);
    }
}
