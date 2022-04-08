package i2f.core.collection.adapter;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/4/1 9:31
 * @desc
 */
public class ArrayIteratorAdapter<T> implements Iterator<T> {
    private T[] enums;
    private int idx;
    public ArrayIteratorAdapter(T[] enums){
        this.enums=enums;
        this.idx=0;
    }
    @Override
    public boolean hasNext() {
        return this.idx<enums.length;
    }

    @Override
    public T next() {
        T item=enums[idx];
        idx++;
        return item;
    }
}
