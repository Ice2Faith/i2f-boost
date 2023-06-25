package i2f.core.container.iterator.impl;

import java.util.Iterator;

public class IteratorIterable<T> implements Iterable<T> {
    private Iterator<T> iterator;

    public IteratorIterable(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<T> iterator() {
        return this.iterator;
    }
}
