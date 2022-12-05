package i2f.core.streaming.base.sink;

import i2f.core.functional.common.IExecutor;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class EachSinkStreaming<M, E> extends AbsSinkStreaming<Void, M, E> {
    public IExecutor<M> executor;

    public EachSinkStreaming(IExecutor<M> executor) {
        this.executor = executor;
    }

    @Override
    protected Void sink(Iterator<M> iterator, ExecutorService pool) {
        parallelizeProcess(iterator, pool, (item, collector) -> {
            executor.accept(item);
        });
        return null;
    }
}
