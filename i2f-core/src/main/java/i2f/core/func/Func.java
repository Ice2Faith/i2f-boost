package i2f.core.func;


import i2f.core.container.comparator.ComparatorUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/6/25 15:30
 * @desc
 */
public class Func {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(String str) {
        if (isNull(str)) {
            return true;
        }
        str = str.trim();
        return isEmpty(str);
    }

    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(Iterable<?> iter) {
        return iter == null || !iter.iterator().hasNext();
    }

    public static boolean isEmpty(Iterator<?> iter) {
        return iter == null || !iter.hasNext();
    }

    public static boolean isEmpty(Enumeration<?> em) {
        return em == null || !em.hasMoreElements();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(int[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(long[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(short[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(byte[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(char[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(float[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(double[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isArray(Object obj) {
        if (isNull(obj)) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return clazz.isArray();
    }

    public static boolean isType(Object obj, Class<?> type) {
        if (isNull(obj)) {
            return true;
        }
        Class<?> clazz = obj.getClass();
        return clazz.equals(type) || type.isAssignableFrom(clazz);
    }

    public static boolean isEnum(Object obj) {
        if (isNull(obj)) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return clazz.isEnum();
    }

    public static boolean isAnnotation(Object obj) {
        if (isNull(obj)) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return clazz.isAnnotation();
    }

    public static boolean isEmptyArray(Object obj) {
        if (isNull(obj)) {
            return true;
        }
        if (!isArray(obj)) {
            return true;
        }
        return Array.getLength(obj) == 0;
    }

    public static boolean isNot(boolean bl) {
        return !bl;
    }

    public static boolean isBool(Boolean bl) {
        return bl != null && bl;
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, Function<E, T> thenMapper, Function<E, T> elseMapper) {
        if (predicate.test(obj)) {
            return thenMapper.apply(obj);
        }
        return elseMapper.apply(obj);
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, Function<E, T> thenMapper, Supplier<T> elseSupplier) {
        if (predicate.test(obj)) {
            return thenMapper.apply(obj);
        }
        return elseSupplier.get();
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, Supplier<T> thenSupplier, Function<E, T> elseMapper) {
        if (predicate.test(obj)) {
            return thenSupplier.get();
        }
        return elseMapper.apply(obj);
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, Supplier<T> thenSupplier, Supplier<T> elseSupplier) {
        if (predicate.test(obj)) {
            return thenSupplier.get();
        }
        return elseSupplier.get();
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, T thenObj, Supplier<T> elseSupplier) {
        if (predicate.test(obj)) {
            return thenObj;
        }
        return elseSupplier.get();
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, Supplier<T> thenSupplier, T elseObj) {
        if (predicate.test(obj)) {
            return thenSupplier.get();
        }
        return elseObj;
    }

    public static <T, E> T when(E obj, Predicate<E> predicate, T thenObj, T elseObj) {
        if (predicate.test(obj)) {
            return thenObj;
        }
        return elseObj;
    }

    public static <T> T ifNull(T obj, T def) {
        return when(obj, Func::isNull, def, obj);
    }

    public static <T> T ifNull(T obj, Supplier<T> supplier) {
        return when(obj, Func::isNull, supplier, obj);
    }

    public static <T> T ifNull(T obj, T thenObj, T elseObj) {
        return when(obj, Func::isNull, thenObj, elseObj);
    }

    public static <T> T ifNull(T obj, Supplier<T> thenSupplier, Supplier<T> elseSupplier) {
        return when(obj, Func::isNull, thenSupplier, elseSupplier);
    }

    public static String null2Empty(String str) {
        return when(str, Func::isNull, "", str);
    }

    public static String empty2Null(String str) {
        return when(str, Func::isEmpty, null, str);
    }

    public static String blank2Null(String str) {
        return when(str, Func::isBlank, null, str);
    }

    public static <T> void notNull(T obj, Consumer<T> consumer) {
        if (!isNull(obj)) {
            consumer.accept(obj);
        }
    }

    public static <T, R> R notNull(T obj, Function<T, R> thenMapper, R elseObj) {
        if (!isNull(obj)) {
            return thenMapper.apply(obj);
        }
        return elseObj;
    }

    public static <T, R> R notNull(T obj, Function<T, R> thenMapper, Supplier<R> elseSupplier) {
        if (!isNull(obj)) {
            return thenMapper.apply(obj);
        }
        return elseSupplier.get();
    }

    public static <T, R> R notNull(T obj, Function<T, R> thenMapper, Function<T, R> elseMapper) {
        if (!isNull(obj)) {
            return thenMapper.apply(obj);
        }
        return elseMapper.apply(obj);
    }


    public static <T> T max(Comparator<T> comparator, T v1, T... vals) {
        T max = v1;
        for (T item : vals) {
            if (ComparatorUtil.compare(max, item, comparator) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static <T> T min(Comparator<T> comparator, T v1, T... vals) {
        T min = v1;
        for (T item : vals) {
            if (ComparatorUtil.compare(min, item, comparator) < 0) {
                min = item;
            }
        }
        return min;
    }

    public static <T extends Comparable<T>> T max(T v1, T... vals) {
        T max = v1;
        for (T item : vals) {
            if (ComparatorUtil.compare(max, item) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static <T extends Comparable<T>> T min(T v1, T... vals) {
        T min = v1;
        for (T item : vals) {
            if (ComparatorUtil.compare(min, item) < 0) {
                min = item;
            }
        }
        return min;
    }


}
