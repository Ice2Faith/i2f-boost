package i2f.container.iterator.impl;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 9:04
 * @desc
 */
public class ArrayObjectIterator<E> implements Iterator<E> {
    private Object arr;
    private int startIndex = 0;
    private int endIndex;

    public ArrayObjectIterator(Object arr) {
        this.arr = arr;
        this.startIndex = 0;
        this.endIndex = Array.getLength(arr);
    }

    public ArrayObjectIterator(Object arr, int startIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = Array.getLength(arr);
    }

    public ArrayObjectIterator(Object arr, int startIndex, int endIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public boolean hasNext() {
        return this.startIndex < this.endIndex;
    }

    @Override
    public E next() {
        E ret = (E) Array.get(arr, startIndex);
        startIndex++;
        return ret;
    }
}
