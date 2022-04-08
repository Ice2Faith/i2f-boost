package i2f.core.collection.adapter;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/4/1 9:31
 * @desc
 */
public class ReflectArrayIteratorAdapter<T> implements Iterator<T> {
    private Object enums;
    private int idx;
    public ReflectArrayIteratorAdapter(Object enums){
        this.enums=enums;
        if(enums==null || !enums.getClass().isArray()){
            throw new UnsupportedOperationException("object not is array instance!");
        }
        this.idx=0;
    }
    @Override
    public boolean hasNext() {
        return this.idx< Array.getLength(enums);
    }

    @Override
    public T next() {
        T item=(T)Array.get(enums,idx);
        idx++;
        return item;
    }
}
