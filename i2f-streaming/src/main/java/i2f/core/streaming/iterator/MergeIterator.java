package i2f.core.streaming.iterator;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2023/4/23 13:14
 * @desc
 */
public class MergeIterator<E> implements Iterator<E> {
    private final Iterator<Iterator<E>> list;
    private Iterator<E> current;

    public MergeIterator(Iterator<Iterator<E>> list) {
        this.list = list;
    }


    @Override
    public boolean hasNext() {
        if (current == null) {
            if (list.hasNext()) {
                current = list.next();
            } else {
                return false;
            }
        }
        if (current == null) {
            return false;
        }
        if (current.hasNext()) {
            return true;
        } else {
            if (list.hasNext()) {
                current = list.next();
            }
        }
        return current.hasNext();
    }

    @Override
    public E next() {
        return current.next();
    }
}
