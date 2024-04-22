package i2f.container.iterator.impl;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:45
 * @desc
 */
public class ArrayIterator<E> implements Iterator<E> {
    private E[] arr;
    private int startIndex = 0;
    private int endIndex;

    public ArrayIterator(E[] arr) {
        this.arr = arr;
        this.startIndex = 0;
        this.endIndex = arr.length;
    }

    public ArrayIterator(E[] arr, int startIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = arr.length;
    }

    public ArrayIterator(E[] arr, int startIndex, int endIndex) {
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
        E ret = arr[startIndex];
        startIndex++;
        return ret;
    }
}
