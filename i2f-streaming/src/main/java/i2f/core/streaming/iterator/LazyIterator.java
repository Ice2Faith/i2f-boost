package i2f.core.streaming.iterator;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/4/22 21:55
 * @desc
 */
public class LazyIterator<E> implements Iterator<E> {
    private final Supplier<Iterator<E>> supplier;
    private volatile Iterator<E> iterator;

    public LazyIterator(Supplier<Iterator<E>> supplier) {
        this.supplier = supplier;
    }

    protected void prepare() {
        if (iterator == null) {
            synchronized (this) {
                if (iterator == null) {
                    iterator = supplier.get();
                }
            }
        }
    }

    @Override
    public synchronized boolean hasNext() {
        prepare();
        return iterator.hasNext();
    }

    @Override
    public synchronized E next() {
        prepare();
        return iterator.next();
    }

    @Override
    public synchronized void remove() {
        prepare();
        iterator.remove();
    }
}
