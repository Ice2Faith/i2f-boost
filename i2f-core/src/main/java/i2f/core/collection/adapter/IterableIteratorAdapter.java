package i2f.core.collection.adapter;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/4/1 9:31
 * @desc
 */
public class IterableIteratorAdapter<T> implements Iterator<T> {
    private Iterable<T> enums;
    private Iterator<T> iterator;
    public IterableIteratorAdapter(Iterable<T> enums){
        this.enums=enums;
        this.iterator= enums.iterator();
    }
    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public T next() {
        return this.iterator.next();
    }
}
