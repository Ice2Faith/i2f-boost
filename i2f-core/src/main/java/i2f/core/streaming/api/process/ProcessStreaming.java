package i2f.core.streaming.api.process;

import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class ProcessStreaming<E, R> extends AbsStreaming<R, E> {
    private IProcessStreaming<R, E> processor;

    public ProcessStreaming(IProcessStreaming<R, E> processor) {
        this.processor = processor;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator) {
        return processor.apply(iterator);
    }

    @Override
    public void create() {
        this.processor.create();
    }

    @Override
    public void destroy() {
        this.processor.destroy();
    }
}
