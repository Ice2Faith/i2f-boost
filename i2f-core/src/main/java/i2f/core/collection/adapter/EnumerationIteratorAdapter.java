package i2f.core.collection.adapter;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/4/1 9:31
 * @desc
 */
public class EnumerationIteratorAdapter<T> implements Iterator<T> {
    private Enumeration<T> enums;
    public EnumerationIteratorAdapter(Enumeration<T> enums){
        this.enums=enums;
    }
    @Override
    public boolean hasNext() {
        return this.enums.hasMoreElements();
    }

    @Override
    public T next() {
        return this.enums.nextElement();
    }
}
