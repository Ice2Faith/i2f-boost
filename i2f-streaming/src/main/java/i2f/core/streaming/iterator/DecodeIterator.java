package i2f.core.streaming.iterator;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/5/3 18:07
 * @desc
 */
public class DecodeIterator<T, E> implements Iterator<E> {
    private Function<T, E> decoder;
    private Iterator<T> iterator;

    public DecodeIterator(Function<T, E> decoder, Iterator<T> iterator) {
        this.decoder = decoder;
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return decoder.apply(iterator.next());
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
