package i2f.core.streaming.iterator;

import i2f.core.streaming.functional.Callback;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2023/4/22 23:05
 * @desc
 */
public class NotifyIterator<E> implements Iterator<E> {
    private Iterator<E> iterator;
    private Callback finisher;

    public NotifyIterator(Iterator<E> iterator, Callback finisher) {
        this.iterator = iterator;
        this.finisher = finisher;
    }

    @Override
    public boolean hasNext() {
        boolean ok = iterator.hasNext();
        if (!ok) {
            finisher.callback();
        }
        return ok;
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
