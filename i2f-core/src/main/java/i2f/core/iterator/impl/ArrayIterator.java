package i2f.core.iterator.impl;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/16 9:03
 * @desc
 */
public class ArrayIterator<T> implements Iterator<T> {
    protected T[] arr;
    protected int i;

    public ArrayIterator(T[] arr) {
        this.arr = arr;
    }

    @Override
    public boolean hasNext() {
        return arr != null && i < arr.length;
    }

    @Override
    public T next() {
        T ret = arr[i];
        i++;
        return ret;
    }
}
