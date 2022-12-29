package i2f.core.lazy;

import i2f.core.functional.jvf.Supplier;
import i2f.core.iterator.impl.LazyIterator;

import java.util.Iterator;

public class Lazies {
    public static <T> Iterator<T> of(Supplier<Iterator<T>> supplier) {
        return new LazyIterator<T>(supplier);
    }
}
