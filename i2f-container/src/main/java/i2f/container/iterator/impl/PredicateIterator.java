package i2f.container.iterator.impl;

import i2f.container.reference.Reference;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/4/19 9:08
 * @desc
 */
public class PredicateIterator<E> implements Iterator<E> {
    private Iterator<E> iterator;
    private Predicate<E> filter;
    private Reference<E> ref = Reference.nop();

    public PredicateIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public PredicateIterator(Iterator<E> iterator, Predicate<E> filter) {
        this.iterator = iterator;
        this.filter = filter;
    }

    @Override
    public boolean hasNext() {
        if (ref.isValue()) {
            return true;
        }
        ref.toFinish();
        while (iterator.hasNext()) {
            E elem = iterator.next();
            if (filter == null || filter.test(elem)) {
                ref.set(elem);
            }
        }
        return ref.isValue();
    }

    @Override
    public E next() {
        E ret = ref.get();
        ref.toNop();
        return ret;
    }
}
