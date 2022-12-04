package i2f.core.iterator;

import i2f.core.iterator.impl.ArrayIterator;
import i2f.core.iterator.impl.ArrayObjectIterator;
import i2f.core.iterator.impl.EnumerationIterator;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/16 9:07
 * @desc
 */
public class Iterators {
    public static <T> Iterator<T> of(T[] arr) {
        return new ArrayIterator<T>(arr);
    }

    public static <T> Iterator<T> of(Iterable<T> iterable) {
        return iterable.iterator();
    }

    public static <T> Iterator<T> of(Collection<T> col) {
        return col.iterator();
    }

    public static <T> Iterator<T> of(Enumeration<T> enums) {
        return new EnumerationIterator<T>(enums);
    }

    public static <T> Iterator<T> of(Object arr) {
        return new ArrayObjectIterator<T>(arr);
    }
}
