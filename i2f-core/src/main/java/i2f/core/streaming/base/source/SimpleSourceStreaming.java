package i2f.core.streaming.base.source;

import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:02
 * @desc
 */
public class SimpleSourceStreaming<E> extends AbsStreaming<E, E> {
    private Iterator<E> iterator;

    public SimpleSourceStreaming(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<E> apply(Iterator<E> spaceHolder) {
        return this.iterator;
    }
}
