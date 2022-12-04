package i2f.core.streaming.api.source;

import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:02
 * @desc
 */
public class SourceStreaming<E> extends AbsStreaming<E, E> {
    private ISourceStreaming<E> source;

    public SourceStreaming(ISourceStreaming<E> source) {
        this.source = source;
    }

    @Override
    public Iterator<E> apply(Iterator<E> spaceHolder) {
        return this.source.iterator();
    }

    @Override
    public void create() {
        this.source.create();
    }

    @Override
    public void destroy() {
        this.source.destroy();
    }
}
