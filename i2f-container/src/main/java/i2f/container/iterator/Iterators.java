package i2f.container.iterator;

import i2f.container.iterator.impl.*;
import i2f.container.iterator.impl.array.*;

import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:56
 * @desc
 * 提供一个可迭代数据转换为迭代器的方法
 */
public class Iterators {
    public static <E> Iterator<E> of(Iterable<E> iterable) {
        return new WrapIterator<>(iterable.iterator());
    }

    public static <E> Iterator<E> of(Collection<E> collection) {
        return new WrapIterator<>(collection.iterator());
    }

    public static <E> Iterator<E> of(Enumeration<E> enumeration) {
        return new EnumerationIterator<>(enumeration);
    }

    public static <E> Iterator<E> of(Stream<E> stream) {
        return new WrapIterator<>(stream.iterator());
    }

    public static <E> Iterator<E> of(E[] arr) {
        return new ArrayIterator<>(arr);
    }

    public static <E> Iterator<E> of(E[] arr, int startIndex) {
        return new ArrayIterator<>(arr, startIndex);
    }

    public static <E> Iterator<E> of(E[] arr, int startIndex, int endIndex) {
        return new ArrayIterator<>(arr, startIndex, endIndex);
    }

    public static Iterator<Integer> of(int[] arr) {
        return new IntArrayIterator(arr);
    }

    public static Iterator<Integer> of(int[] arr, int startIndex) {
        return new IntArrayIterator(arr, startIndex);
    }

    public static Iterator<Integer> of(int[] arr, int startIndex, int endIndex) {
        return new IntArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Long> of(long[] arr) {
        return new LongArrayIterator(arr);
    }

    public static Iterator<Long> of(long[] arr, int startIndex) {
        return new LongArrayIterator(arr, startIndex);
    }

    public static Iterator<Long> of(long[] arr, int startIndex, int endIndex) {
        return new LongArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Boolean> of(boolean[] arr) {
        return new BooleanArrayIterator(arr);
    }

    public static Iterator<Boolean> of(boolean[] arr, int startIndex) {
        return new BooleanArrayIterator(arr, startIndex);
    }

    public static Iterator<Boolean> of(boolean[] arr, int startIndex, int endIndex) {
        return new BooleanArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Byte> of(byte[] arr) {
        return new ByteArrayIterator(arr);
    }

    public static Iterator<Byte> of(byte[] arr, int startIndex) {
        return new ByteArrayIterator(arr, startIndex);
    }

    public static Iterator<Byte> of(byte[] arr, int startIndex, int endIndex) {
        return new ByteArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Character> of(char[] arr) {
        return new CharArrayIterator(arr);
    }

    public static Iterator<Character> of(char[] arr, int startIndex) {
        return new CharArrayIterator(arr, startIndex);
    }

    public static Iterator<Character> of(char[] arr, int startIndex, int endIndex) {
        return new CharArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Double> of(double[] arr) {
        return new DoubleArrayIterator(arr);
    }

    public static Iterator<Double> of(double[] arr, int startIndex) {
        return new DoubleArrayIterator(arr, startIndex);
    }

    public static Iterator<Double> of(double[] arr, int startIndex, int endIndex) {
        return new DoubleArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Float> of(float[] arr) {
        return new FloatArrayIterator(arr);
    }

    public static Iterator<Float> of(float[] arr, int startIndex) {
        return new FloatArrayIterator(arr, startIndex);
    }

    public static Iterator<Float> of(float[] arr, int startIndex, int endIndex) {
        return new FloatArrayIterator(arr, startIndex, endIndex);
    }

    public static Iterator<Short> of(short[] arr) {
        return new ShortArrayIterator(arr);
    }

    public static Iterator<Short> of(short[] arr, int startIndex) {
        return new ShortArrayIterator(arr, startIndex);
    }

    public static Iterator<Short> of(short[] arr, int startIndex, int endIndex) {
        return new ShortArrayIterator(arr, startIndex, endIndex);
    }

    public static <E> Iterator<E> ofArrayObject(Object arr) {
        return new ArrayObjectIterator<>(arr);
    }

    public static <E> Iterator<E> ofArrayObject(Object arr, int startIndex) {
        return new ArrayObjectIterator<>(arr, startIndex);
    }

    public static <E> Iterator<E> ofArrayObject(Object arr, int startIndex, int endIndex) {
        return new ArrayObjectIterator<>(arr, startIndex, endIndex);
    }

    public static Iterator<String> ofReaderLine(Reader reader) {
        return new ReaderLineIterator(reader);
    }

    public static Iterator<String> ofReaderLine(BufferedReader reader) {
        return new ReaderLineIterator(reader);
    }

    public static Iterator<String> ofReaderLine(InputStream is, String charset) throws IOException {
        return new ReaderLineIterator(is, charset);
    }

    public static Iterator<String> ofReaderLine(File file, String charset) throws IOException {
        return new FileLineIterator(file, charset);
    }
}
