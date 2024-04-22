package i2f.container.enumeration.impl;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:55
 * @desc
 */
public class IteratorEnumeration<E> implements Enumeration<E> {
    private Iterator<E> iterator;

    public IteratorEnumeration(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    @Override
    public E nextElement() {
        return iterator.next();
    }
}
