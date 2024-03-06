package i2f.stream.impl;


import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/2/23 13:55
 * @desc
 */
public class MergeIterator<E> implements Iterator<E> {

    private LinkedList<Iterator<E>> list;

    public MergeIterator(Collection<Iterator<E>> list) {
        this.list = new LinkedList<>(list);
    }

    public MergeIterator(Iterator<E> ... list) {
        this.list=new LinkedList<>();
        for (Iterator<E> iterator : list) {
            this.list.add(iterator);
        }
    }

    protected Iterator<E> getIterator() {
        Iterator<E> curr = null;
        while (true) {
            if (this.list.isEmpty()) {
                break;
            }
            curr = this.list.getFirst();
            if (curr.hasNext()) {
                break;
            }
            curr = null;
            this.list.removeFirst();
        }
        return curr;
    }

    @Override
    public boolean hasNext() {
        Iterator<E> curr = getIterator();
        return curr!=null && curr.hasNext();
    }

    @Override
    public E next() {
        Iterator<E> curr = getIterator();
        if(curr==null){
            throw new NullPointerException("not more iterator");
        }
        return curr.next();
    }
}
