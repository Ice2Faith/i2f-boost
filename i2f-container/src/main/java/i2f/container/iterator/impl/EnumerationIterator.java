package i2f.container.iterator.impl;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:42
 * @desc
 */
public class EnumerationIterator<E> implements Iterator<E> {
    private Enumeration<E> enumeration;

    public EnumerationIterator(Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        return enumeration.nextElement();
    }

}
