package i2f.core.iterator.impl;

import i2f.core.functional.jvf.Supplier;
import i2f.core.lazy.Lazyable;

import java.util.Iterator;

public class LazyIterator<T> implements Iterator<T>, Lazyable {
    private volatile boolean isRequire = false;
    private Supplier<Iterator<T>> supplier;
    private Iterator<T> iterator;

    public LazyIterator(Supplier<Iterator<T>> supplier) {
        this.supplier = supplier;
    }

    private void requireCheck() {
        if (isRequire) {
            return;
        }
        synchronized (this) {
            if (!isRequire) {
                this.iterator = supplier.get();
            }
            isRequire = true;
        }
    }

    @Override
    public boolean hasNext() {
        requireCheck();
        return iterator.hasNext();
    }

    @Override
    public T next() {
        requireCheck();
        return iterator.next();
    }

    @Override
    public void remove() {
        requireCheck();
        iterator.remove();
    }
}
