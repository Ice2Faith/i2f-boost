package i2f.core.streaming.api.sink;

import i2f.core.streaming.base.sink.AbsSinkStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class SinkStreaming<R, E> extends AbsSinkStreaming<R, E, E> {
    private ISinkStreaming<R, E> sink;

    public SinkStreaming(ISinkStreaming<R, E> sink) {
        this.sink = sink;
    }

    @Override
    protected R sink(Iterator<E> iterator, ExecutorService pool) {
        return this.sink.sink(iterator, pool);
    }

    @Override
    public void create() {
        this.sink.create();
    }

    @Override
    public void destroy() {
        this.sink.destroy();
    }
}
