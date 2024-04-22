package i2f.container.iterator.impl;

import java.util.Iterator;
import java.util.function.Function;


/**
 * @author Ice2Faith
 * @date 2024/4/19 9:08
 * @desc
 */
public class FunctionIterator<E, R> implements Iterator<R> {
    private Iterator<E> iterator;
    private Function<E, R> function;


    public FunctionIterator(Iterator<E> iterator, Function<E, R> function) {
        this.iterator = iterator;
        this.function = function;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R next() {
        E ret = iterator.next();
        return function.apply(ret);
    }
}
