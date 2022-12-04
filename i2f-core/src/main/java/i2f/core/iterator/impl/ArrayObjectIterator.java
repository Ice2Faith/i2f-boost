package i2f.core.iterator.impl;

import i2f.core.array.Arrays;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/16 9:03
 * @desc
 */
public class ArrayObjectIterator<T> implements Iterator<T> {
    protected Object arr;
    protected int i = 0;

    public ArrayObjectIterator(Object arr) {
        this.arr = arr;
    }

    @Override
    public boolean hasNext() {
        if (Arrays.isArray(arr)) {
            return i < Array.getLength(arr);
        }
        return i == 0;
    }

    @Override
    public T next() {
        if (Arrays.isArray(arr)) {
            T ret = (T) Array.get(arr, i);
            i++;
            return ret;
        }
        i++;
        return (T) arr;
    }
}
