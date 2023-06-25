package i2f.core.container.iterator.impl;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/16 9:03
 * @desc
 */
public class EnumerationIterator<T> implements Iterator<T> {
    protected Enumeration<T> enums;

    public EnumerationIterator(Enumeration<T> enums) {
        this.enums = enums;
    }

    @Override
    public boolean hasNext() {
        return enums != null && enums.hasMoreElements();
    }

    @Override
    public T next() {
        return enums.nextElement();
    }
}
