package i2f.core.streaming.api.sink;

import i2f.core.streaming.base.sink.AbsSinkStreaming;

import java.util.Iterator;

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
    protected R sink(Iterator<E> iterator) {
        return this.sink.sink(iterator);
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
