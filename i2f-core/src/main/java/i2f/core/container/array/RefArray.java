package i2f.core.container.array;


import i2f.core.container.iterator.impl.ArrayObjectIterator;

import java.lang.reflect.Array;
import java.util.Iterator;

public class RefArray<T> implements Iterable<T> {
    private Object arr;

    public RefArray(Object arr) {
        this.arr = arr;
    }

    public boolean isArray() {
        return ArrayUtil.isArray(arr);
    }

    public int length() {
        if (ArrayUtil.isArray(arr)) {
            return Array.getLength(arr);
        }
        return 1;
    }

    public T get(int i) {
        if (ArrayUtil.isArray(arr)) {
            return (T) Array.get(arr, i);
        }
        if (i == 0) {
            return (T) arr;
        }
        throw new ArrayIndexOutOfBoundsException(length());
    }

    public void set(int i, T elem) {
        if (ArrayUtil.isArray(arr)) {
            Array.set(arr, i, elem);
            return;
        }
        if (i == 0) {
            arr = elem;
            return;
        }
        throw new ArrayIndexOutOfBoundsException(length());
    }

    public Object getArray() {
        return arr;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayObjectIterator<>(arr);
    }
}
