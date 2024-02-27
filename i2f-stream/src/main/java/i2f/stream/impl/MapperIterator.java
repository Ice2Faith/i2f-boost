package i2f.stream.impl;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/2/23 14:14
 * @desc
 */
public class MapperIterator<T,R> implements Iterator<R> {

    protected Iterator<T> iterator;
    protected Function<T,R> mapper;

    public MapperIterator(Iterator<T> iterator, Function<T, R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public R next() {
        T elem = this.iterator.next();
        R ret = this.mapper.apply(elem);
        return ret;
    }
}
