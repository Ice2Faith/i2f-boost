package i2f.stream.impl;

import java.security.SecureRandom;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/2/23 13:55
 * @desc
 */
public class MixedIterator<E> implements Iterator<E> {

    private SecureRandom random=new SecureRandom();
    private LinkedList<Iterator<E>> list;

    public MixedIterator(Collection<Iterator<E>> list) {
        this.list = new LinkedList<>(list);
    }

    public MixedIterator(Iterator<E> ... list) {
        this.list=new LinkedList<>();
        for (Iterator<E> iterator : list) {
            this.list.add(iterator);
        }
    }

    @Override
    public boolean hasNext() {
        for (Iterator<E> iterator : list) {
            if(iterator.hasNext()){
                return true;
            }
        }
        return false;
    }

    @Override
    public E next() {
        List<Iterator<E>> tmp=new ArrayList<>();
        for (Iterator<E> iterator : list) {
            if(iterator.hasNext()){
                tmp.add(iterator);
            }
        }
        int idx = random.nextInt(tmp.size());
        return tmp.get(idx).next();
    }
}
