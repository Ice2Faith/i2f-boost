package i2f.core.container.iterator;

import i2f.core.container.iterator.impl.IteratorIterable;
import i2f.core.streaming.Streaming;
import i2f.core.type.tuple.ITuple;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class Iterables {
    public static <T> Iterable<T> of(Iterator<T> iterator) {
        return new IteratorIterable<>(iterator);
    }

    public static <T> Iterable<T> ofArgs(T... arr) {
        return of(Iterators.of(arr));
    }

    public static <T> Iterable<T> of(T[] arr) {
        return of(Iterators.of(arr));
    }

    public static <T> Iterable<T> of(Iterable<T> iterable) {
        return iterable;
    }

    public static <T> Iterable<T> of(Collection<T> col) {
        return col;
    }

    public static <T> Iterable<T> of(Enumeration<T> enums) {
        return of(Iterators.of(enums));
    }

    public static <T> Iterable<T> ofArray(Object arr) {
        return of(Iterators.ofArray(arr));
    }

    public static <K, V> Iterable<Map.Entry<K, V>> of(Map<K, V> map) {
        return map.entrySet();
    }

    public static <T> Iterable<T> of(Stream<T> stream) {
        return of(Iterators.of(stream));
    }

    public static <T> Iterable<T> of(Streaming<T> streaming) {
        return of(Iterators.of(streaming));
    }

    public static Iterable<Object> of(ITuple tuple) {
        return tuple.toList();
    }

    public static Iterable<String> of(Reader reader) {
        return of(Iterators.of(reader));
    }

    public static Iterable<String> of(InputStream is, Charset charset) {
        return of(Iterators.of(is, charset));
    }

    public static Iterable<Byte> ofByte(InputStream is) {
        return of(Iterators.ofByte(is));
    }

    public static Iterable<Character> ofChar(Reader reader) {
        return of(Iterators.ofChar(reader));
    }

    public static Iterable<Character> ofChar(InputStream is, Charset charset) {
        return of(Iterators.ofChar(is, charset));
    }

    public static Iterable<Character> of(String str) {
        return of(Iterators.of(str));
    }

    public static <T> Iterable<T> ofMultiArray(Object obj) {
        return of(Iterators.ofMultiArray(obj));
    }

}
