package i2f.container.iterator.impl;

import java.util.Iterator;
import java.util.function.Consumer;


/**
 * @author Ice2Faith
 * @date 2024/4/19 9:08
 * @desc
 */
public class ConsumerIterator<E> implements Iterator<E> {
    private Iterator<E> iterator;
    private Consumer<E> consumer;


    public ConsumerIterator(Iterator<E> iterator, Consumer<E> consumer) {
        this.iterator = iterator;
        this.consumer = consumer;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        E ret = iterator.next();
        consumer.accept(ret);
        return ret;
    }
}
