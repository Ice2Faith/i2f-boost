package i2f.core.collection;

import i2f.core.annotations.notice.CloudBe;
import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.data.Pair;
import i2f.core.functional.common.IMapper;
import i2f.core.functional.jvf.Predicate;
import i2f.core.iterator.Iterators;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ltb
 * @date 2022/3/22 19:32
 * @desc
 */
@Author("i2f")
public class Collections {

    public static int hashSize(int size) {
        if (size < 32) {
            return 32;
        }
        return (int) (size / 0.75);
    }

    public static boolean isEmpty(Collection<?> col) {
        return col == null || !col.iterator().hasNext();
    }

    public static <T, C extends Collection<T>> C collect(C col, T... args) {
        return collect(col, Iterators.of(args));
    }

    public static <T, C extends Collection<T>> C collect(C col, @Nullable Predicate<T> filter, T... args) {
        return collect(col, Iterators.of(args), filter);
    }

    public static <T, C extends Collection<T>> C collect(C col, @Nullable Predicate<T> filter, @CloudBe("-1") int offset, @CloudBe("-1") int size, T... args) {
        return collect(col, Iterators.of(args), filter, offset, size);
    }

    public static <T, C extends Collection<T>> C collect(C col, Iterable<T> iterable) {
        return collect(col, Iterators.of(iterable));
    }

    public static <T, C extends Collection<T>> C collect(C col, Iterable<T> iterable, @Nullable Predicate<T> filter) {
        return collect(col, Iterators.of(iterable), filter);
    }

    public static <T, C extends Collection<T>> C collect(C col, Iterable<T> iterable, @Nullable Predicate<T> filter, @CloudBe("-1") int offset, @CloudBe("-1") int size) {
        return collect(col, Iterators.of(iterable), filter, offset, size);
    }

    public static <T, C extends Collection<T>> C collect(C col, Collection<T> collection) {
        return collect(col, Iterators.of(collection));
    }

    public static <T, C extends Collection<T>> C collect(C col, Collection<T> collection, @Nullable Predicate<T> filter) {
        return collect(col, Iterators.of(collection), filter);
    }

    public static <T, C extends Collection<T>> C collect(C col, Collection<T> collection, @Nullable Predicate<T> filter, @CloudBe("-1") int offset, @CloudBe("-1") int size) {
        return collect(col, Iterators.of(collection), filter, offset, size);
    }

    public static <T, C extends Collection<T>> C collect(C col, Enumeration<T> enums) {
        return collect(col, Iterators.of(enums));
    }

    public static <T, C extends Collection<T>> C collect(C col, Enumeration<T> enums, @Nullable Predicate<T> filter) {
        return collect(col, Iterators.of(enums), filter);
    }

    public static <T, C extends Collection<T>> C collect(C col, Enumeration<T> enums, @Nullable Predicate<T> filter, @CloudBe("-1") int offset, @CloudBe("-1") int size) {
        return collect(col, Iterators.of(enums), filter, offset, size);
    }

    public static <T, C extends Collection<T>> C collect(C col, Object arr) {
        return collect(col, Iterators.of(arr));
    }

    public static <T, C extends Collection<T>> C collect(C col, Object arr, @Nullable Predicate<T> filter) {
        return collect(col, Iterators.of(arr), filter);
    }

    public static <T, C extends Collection<T>> C collect(C col, Object arr, @Nullable Predicate<T> filter, @CloudBe("-1") int offset, @CloudBe("-1") int size) {
        return collect(col, Iterators.of(arr), filter, offset, size);
    }

    public static <T, C extends Collection<T>> C collect(C col, Iterator<T> iterator) {
        return collect(col, iterator, null);
    }

    public static <T, C extends Collection<T>> C collect(C col, Iterator<T> iterator, @Nullable Predicate<T> filter) {
        return collect(col, iterator, filter, -1, -1);
    }

    public static <T, C extends Collection<T>> C collect(C col, Iterator<T> iterator, @Nullable Predicate<T> filter, @CloudBe("-1") int offset, @CloudBe("-1") int size) {
        int idx = 0;
        int cnt = 0;
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (offset < 0 || idx >= offset) {
                if (size < 0 || cnt < size) {
                    if (filter == null || filter.test(item)) {
                        col.add(item);
                    }
                }
                cnt++;
            }

        }
        return col;
    }


    public static <K, V, M extends Map<K, V>, E extends Collection<Pair<K, V>>> E collect(E dst, M map) {
        if (map == null) {
            return dst;
        }
        for (Map.Entry<K, V> item : map.entrySet()) {
            Pair<K, V> pair = new Pair<>(item.getKey(), item.getValue());
            dst.add(pair);
        }
        return dst;
    }

    public static <T, E extends Collection<T>> E collect(E dst, T[] src, int from, int size) {
        return collect(dst, src, from, size, null, null);
    }

    public static <T, E extends Collection<T>> E collect(E dst, Enumeration<T> src, int from, int size) {
        return collect(dst, src, from, size, null, null);
    }

    public static <T, E extends Collection<T>> E collect(E dst, Iterable<T> src, int from, int size) {
        return collect(dst, src, from, size, null, null);
    }

    public static <T, E extends Collection<T>> E collect(E dst, Iterator<T> src, int from, int size) {
        return collect(dst, src, from, size, null, null);
    }

    public static <V, T, E extends Collection<T>> E collect(E dst, V[] src, int from, @CloudBe("-1") int size, @Nullable Predicate<V> filter, @Nullable IMapper<T, V> mapper) {
        return collect(dst, Iterators.of(src), from, size, filter, mapper);
    }

    public static <V, T, E extends Collection<T>> E collect(E dst, Enumeration<V> src, int from, @CloudBe("-1") int size, @Nullable Predicate<V> filter, @Nullable IMapper<T, V> mapper) {
        return collect(dst, Iterators.of(src), from, size, filter, mapper);
    }

    public static <V, T, E extends Collection<T>> E collect(E dst, Iterable<V> src, int from, @CloudBe("-1") int size, @Nullable Predicate<V> filter, @Nullable IMapper<T, V> mapper) {
        return collect(dst, Iterators.of(src), from, size, filter, mapper);
    }

    public static <V, T, E extends Collection<T>> E collect(E dst, Iterator<V> src, int from, @CloudBe("-1") int size, @Nullable Predicate<V> filter, @Nullable IMapper<T, V> mapper) {
        if (dst == null || src == null) {
            return dst;
        }
        Iterator<V> iterator = src;
        boolean isFilterNull = (filter == null);
        boolean isMapperNull = (mapper == null);
        int count = 0;
        int index = 0;
        while (iterator.hasNext()) {
            V val = iterator.next();
            if (index >= from) {
                if (size > 0 && count >= size) {
                    break;
                }
                if (isFilterNull || filter.test(val)) {
                    T sval = isMapperNull ? ((T) val) : mapper.get(val);
                    dst.add(sval);
                }
                count++;
            }
            index++;
        }
        return dst;
    }


    public static <T, C extends Collection<T>> C merge(C col, T[]... arrs) {
        Iterator<T>[] iterators = new Iterator[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            iterators[i] = Iterators.of(arrs[i]);
        }
        return merge(col, iterators);
    }

    public static <T, E extends Collection<T>> E merge(E dst, T[] src1, T... src2) {
        collect(dst, Iterators.of(src1));
        collect(dst, Iterators.of(src2));
        return dst;
    }

    public static <T, C extends Collection<T>> C merge(C col, Enumeration<T>... enumerations) {
        Iterator<T>[] iterators = new Iterator[enumerations.length];
        for (int i = 0; i < enumerations.length; i++) {
            iterators[i] = Iterators.of(enumerations[i]);
        }
        return merge(col, iterators);
    }

    public static <T, C extends Collection<T>> C merge(C col, Iterable<T>... iterables) {
        Iterator<T>[] iterators = new Iterator[iterables.length];
        for (int i = 0; i < iterables.length; i++) {
            iterators[i] = Iterators.of(iterables[i]);
        }
        return merge(col, iterators);
    }

    public static <T, C extends Collection<T>> C merge(C col, Collection<T>... collections) {
        Iterator<T>[] iterators = new Iterator[collections.length];
        for (int i = 0; i < collections.length; i++) {
            iterators[i] = Iterators.of(collections[i]);
        }
        return merge(col, iterators);
    }

    public static <T, C extends Collection<T>> C merge(C col, Iterator<T>... iterators) {
        for (Iterator<T> iterator : iterators) {
            if (iterator == null) {
                continue;
            }
            while (iterator.hasNext()) {
                col.add(iterator.next());
            }
        }
        return col;
    }

    public static <T> ArrayList<T> arrayList(T... args) {
        return collect(new ArrayList<T>(), args);
    }

    public static <T> LinkedList<T> linkedList(T... args) {
        return collect(new LinkedList<T>(), args);
    }

    public static <T> HashSet<T> hashSet(T... args) {
        return collect(new HashSet<T>(), args);
    }

    public static <T> LinkedHashSet<T> linkedHashSet(T... args) {
        return collect(new LinkedHashSet<T>(), args);
    }


    public static <T> Vector<T> vector(T... data) {
        return collect(new Vector<>(), data);
    }

    public static <T> TreeSet<T> treeSet(T... data) {
        return collect(new TreeSet<T>(), data);
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayList(T... data) {
        return collect(new CopyOnWriteArrayList<T>(), data);
    }

    public static <T> CopyOnWriteArraySet<T> copyOnWriteArraySet(T... data) {
        return collect(new CopyOnWriteArraySet<T>(), data);
    }

    public static <T> ArrayList<T> arrayList(Iterable<T> data) {
        return collect(new ArrayList<T>(), data);
    }

    public static <T> LinkedList<T> linkedList(Iterable<T> data) {
        return collect(new LinkedList<>(), data);
    }

    public static <T> Vector<T> vector(Iterable<T> data) {
        return collect(new Vector<>(), data);
    }

    public static <T> HashSet<T> hashSet(Iterable<T> data) {
        return collect(new HashSet<T>(), data);
    }

    public static <T> TreeSet<T> treeSet(Iterable<T> data) {
        return collect(new TreeSet<T>(), data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(Iterable<T> data){
        return collect(new LinkedHashSet<T>(), data);
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayList(Iterable<T> data) {
        return collect(new CopyOnWriteArrayList<T>(), data);
    }

    public static <T> CopyOnWriteArraySet<T> copyOnWriteArraySet(Iterable<T> data) {
        return collect(new CopyOnWriteArraySet<T>(), data);
    }

    public static <T> ArrayList<T> arrayList(Enumeration<T> data) {
        return collect(new ArrayList<T>(), data);
    }

    public static <T> LinkedList<T> linkedList(Enumeration<T> data) {
        return collect(new LinkedList<>(), data);
    }

    public static <T> Vector<T> vector(Enumeration<T> data) {
        return collect(new Vector<>(), data);
    }

    public static <T> HashSet<T> hashSet(Enumeration<T> data) {
        return collect(new HashSet<T>(), data);
    }

    public static <T> TreeSet<T> treeSet(Enumeration<T> data) {
        return collect(new TreeSet<T>(), data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(Enumeration<T> data){
        return collect(new LinkedHashSet<T>(), data);
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayList(Enumeration<T> data) {
        return collect(new CopyOnWriteArrayList<T>(), data);
    }

    public static <T> CopyOnWriteArraySet<T> copyOnWriteArraySet(Enumeration<T> data) {
        return collect(new CopyOnWriteArraySet<T>(), data);
    }

    public static <T> ArrayList<T> arrayList(Iterator<T> data) {
        return collect(new ArrayList<T>(), data);
    }

    public static <T> LinkedList<T> linkedList(Iterator<T> data) {
        return collect(new LinkedList<>(), data);
    }

    public static <T> Vector<T> vector(Iterator<T> data) {
        return collect(new Vector<>(), data);
    }

    public static <T> HashSet<T> hashSet(Iterator<T> data) {
        return collect(new HashSet<T>(), data);
    }

    public static <T> TreeSet<T> treeSet(Iterator<T> data) {
        return collect(new TreeSet<T>(), data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(Iterator<T> data){
        return collect(new LinkedHashSet<T>(), data);
    }
    public static<T> CopyOnWriteArrayList<T> copyOnWriteArrayList(Iterator<T> data){
        return collect(new CopyOnWriteArrayList<T>(), data);
    }
    public static<T> CopyOnWriteArraySet<T> copyOnWriteArraySet(Iterator<T> data){
        return collect(new CopyOnWriteArraySet<T>(), data);
    }


}
