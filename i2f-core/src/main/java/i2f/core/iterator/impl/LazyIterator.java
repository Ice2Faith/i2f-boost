package i2f.core.iterator.impl;

import i2f.core.functional.jvf.Supplier;

import java.util.Iterator;

public class LazyIterator<T> implements Iterator<T> {
    private boolean isRequire = false;
    private Supplier<Iterator<T>> iteratorSupplier;
    private Iterator<T> realIterator;

    public LazyIterator(Supplier<Iterator<T>> iteratorSupplier) {
        this.iteratorSupplier = iteratorSupplier;
    }

    private void requireCheck() {
        if (!isRequire) {
            this.realIterator = iteratorSupplier.get();
        }
        isRequire = true;
    }

    @Override
    public boolean hasNext() {
        requireCheck();
        isRequire = true;
        return realIterator.hasNext();
    }

    @Override
    public T next() {
        requireCheck();
        return realIterator.next();
    }

    @Override
    public void remove() {
        requireCheck();
        realIterator.remove();
    }
}
