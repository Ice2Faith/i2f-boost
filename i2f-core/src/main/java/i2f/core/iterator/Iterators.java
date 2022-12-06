package i2f.core.iterator;

import i2f.core.iterator.impl.ArrayIterator;
import i2f.core.iterator.impl.ArrayObjectIterator;
import i2f.core.iterator.impl.EnumerationIterator;
import i2f.core.iterator.impl.LineReaderIterator;
import i2f.core.streaming.Streaming;
import i2f.core.tuple.ITuple;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

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

    public static <K, V> Iterator<Map.Entry<K, V>> of(Map<K, V> map) {
        return map.entrySet().iterator();
    }

    public static <T> Iterator<T> of(Stream<T> stream) {
        return stream.iterator();
    }

    public static <T> Iterator<T> of(Streaming<T> streaming) {
        return streaming.iterator();
    }

    public static Iterator<Object> of(ITuple tuple) {
        return tuple.toList().iterator();
    }

    public static Iterator<String> of(Reader reader) {
        return new LineReaderIterator(reader);
    }

    public static Iterator<String> of(InputStream is, Charset charset) {
        return new LineReaderIterator(is, charset);
    }

}
