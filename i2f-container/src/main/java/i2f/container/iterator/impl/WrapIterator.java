package i2f.container.iterator.impl;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:38
 * @desc
 */
public class WrapIterator<E> implements Iterator<E> {
    private Iterator<E> iterator;

    public WrapIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        iterator.forEachRemaining(action);
    }
}
