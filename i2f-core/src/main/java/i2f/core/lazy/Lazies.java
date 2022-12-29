package i2f.core.lazy;

import i2f.core.functional.jvf.Supplier;
import i2f.core.iterator.impl.LazyIterator;
import i2f.core.stream.LazyInputStream;
import i2f.core.stream.LazyOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class Lazies {
    public static <T> Iterator<T> ofIterator(Supplier<Iterator<T>> supplier) {
        return new LazyIterator<T>(supplier);
    }

    public static InputStream ofInputStream(Supplier<InputStream> supplier) {
        return new LazyInputStream(supplier);
    }

    public static OutputStream ofOutputStream(Supplier<OutputStream> supplier) {
        return new LazyOutputStream(supplier);
    }
}
