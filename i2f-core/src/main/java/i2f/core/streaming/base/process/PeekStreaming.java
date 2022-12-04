package i2f.core.streaming.base.process;

import i2f.core.functional.jvf.Consumer;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class PeekStreaming<E> extends AbsStreaming<E, E> {
    private Consumer<E> consumer;

    public PeekStreaming(Consumer<E> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator) {
        List<E> ret = new LinkedList<E>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            ret.add(item);
            consumer.accept(item);
        }
        return ret.iterator();
    }
}
