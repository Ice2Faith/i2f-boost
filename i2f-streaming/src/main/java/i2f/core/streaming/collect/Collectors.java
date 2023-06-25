package i2f.core.streaming.collect;

import i2f.core.container.collection.ConcurrentHashSet;
import i2f.core.streaming.Streaming;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Collectors {

    public static final Set<Collector.Characteristics> CH_CONCURRENT_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_CONCURRENT_NOID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED));
    public static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_UNORDERED_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    public static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return toCollection(new ArrayList<T>(), CH_ID);
    }

    public static <T> Collector<T, ?, ArrayList<T>> toArrayList(int capital) {
        return toCollection(new ArrayList<T>(Math.max(capital, 32)), CH_ID);
    }

    public static <T> Collector<T, ?, LinkedList<T>> toLinkedList() {
        return toCollection(new LinkedList<T>(), CH_ID);
    }

    public static <T> Collector<T, ?, HashSet<T>> toHashSet() {
        return toCollection(new HashSet<T>(), CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, HashSet<T>> toHashSet(int capital) {
        return toCollection(new HashSet<T>(Math.max(capital, 32)), CH_UNORDERED_ID);
    }

    public static <T extends Comparable<T>> Collector<T, ?, TreeSet<T>> toTreeSet() {
        return toCollection(new TreeSet<T>(), CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, TreeSet<T>> toTreeSet(Comparator<T> comparator) {
        return toCollection(new TreeSet<T>(comparator), CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, LinkedHashSet<T>> toLinkedHashSet() {
        return toCollection(new LinkedHashSet<T>(), CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, ConcurrentHashSet<T>> toConcurrentHashSet() {
        return toCollection(new ConcurrentHashSet<T>(), CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, ConcurrentHashSet<T>> toConcurrentHashSet(int capital) {
        return toCollection(new ConcurrentHashSet<T>(Math.max(capital, 32)), CH_UNORDERED_ID);
    }

    public static <T> Collector<T, ?, CopyOnWriteArrayList<T>> toCopyOnWriteArrayList() {
        return toCollection(new CopyOnWriteArrayList<T>(), CH_CONCURRENT_ID);
    }

    public static <T> Collector<T, ?, CopyOnWriteArraySet<T>> toCopyOnWriteArraySet() {
        return toCollection(new CopyOnWriteArraySet<T>(), CH_CONCURRENT_NOID);
    }

    public static <ELEM, COL extends Collection<ELEM>> Collector<ELEM, ?, COL> toCollection(COL col) {
        return toCollection(new Supplier<COL>() {
            @Override
            public COL get() {
                return col;
            }
        });
    }

    public static <ELEM, COL extends Collection<ELEM>> Collector<ELEM, ?, COL> toCollection(COL col, Set<Collector.Characteristics> characteristics) {
        return toCollection(new Supplier<COL>() {
            @Override
            public COL get() {
                return col;
            }
        }, characteristics);
    }

    public static <ELEM, COL extends Collection<ELEM>> Collector<ELEM, ?, COL> toCollection(Supplier<COL> collectionSupplier) {
        return toCollection(collectionSupplier, CH_ID);
    }

    public static <ELEM, COL extends Collection<ELEM>> Collector<ELEM, ?, COL> toCollection(Supplier<COL> collectionSupplier, Set<Collector.Characteristics> characteristics) {
        return new CollectorImpl<>(collectionSupplier, Collection<ELEM>::add,
                (col, elems) -> {
                    col.addAll(elems);
                    return col;
                },
                characteristics);
    }

    public static <ELEM> Collector<ELEM, ?, Streaming<ELEM>> toStreaming() {
        return toStreaming(new LinkedList<ELEM>(), CH_NOID);
    }

    public static <ELEM> Collector<ELEM, ?, Streaming<ELEM>> toStreaming(Collection<ELEM> col, Set<Collector.Characteristics> characteristics) {
        return new CollectorImpl<>(new Supplier<Collection<ELEM>>() {
            @Override
            public Collection<ELEM> get() {
                return col;
            }
        }, Collection<ELEM>::add, (col1, col2) -> {
            col1.addAll(col2);
            return col1;
        }, new Function<Collection<ELEM>, Streaming<ELEM>>() {
            @Override
            public Streaming<ELEM> apply(Collection<ELEM> elems) {
                return Streaming.source(elems);
            }
        }, characteristics);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, HashMap<KEY, VAL>> toHashMap(
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new HashMap<KEY, VAL>(), keyMapper, valueMapper, true, CH_UNORDERED_ID);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, HashMap<KEY, VAL>> toHashMap(
            int capital,
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new HashMap<KEY, VAL>(Math.max(capital, 32)), keyMapper, valueMapper, true, CH_UNORDERED_ID);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, LinkedHashMap<KEY, VAL>> toLinkedHashMap(
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new LinkedHashMap<KEY, VAL>(), keyMapper, valueMapper, true, CH_UNORDERED_ID);
    }

    public static <ELEM, KEY extends Comparable<KEY>, VAL> Collector<ELEM, ?, TreeMap<KEY, VAL>> toTreeMap(
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new TreeMap<KEY, VAL>(), keyMapper, valueMapper, true, CH_UNORDERED_ID);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, TreeMap<KEY, VAL>> toTreeMap(
            Comparator<KEY> keyComparator,
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new TreeMap<KEY, VAL>(keyComparator), keyMapper, valueMapper, true, CH_UNORDERED_ID);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, ConcurrentHashMap<KEY, VAL>> toConcurrentHashMap(
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new ConcurrentHashMap<KEY, VAL>(), keyMapper, valueMapper, true, CH_CONCURRENT_NOID);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, ConcurrentHashMap<KEY, VAL>> toConcurrentHashMap(
            int capital,
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new ConcurrentHashMap<KEY, VAL>(Math.max(capital, 32)), keyMapper, valueMapper, true, CH_CONCURRENT_NOID);
    }

    public static <ELEM, KEY extends Comparable<KEY>, VAL> Collector<ELEM, ?, ConcurrentSkipListMap<KEY, VAL>> toConcurrentSkipListMap(
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new ConcurrentSkipListMap<KEY, VAL>(), keyMapper, valueMapper, true, CH_CONCURRENT_NOID);
    }

    public static <ELEM, KEY, VAL> Collector<ELEM, ?, ConcurrentSkipListMap<KEY, VAL>> toConcurrentSkipListMap(
            Comparator<KEY> keyComparator,
            Function<ELEM, KEY> keyMapper,
            Function<ELEM, VAL> valueMapper
    ) {
        return toMap(new ConcurrentSkipListMap<KEY, VAL>(keyComparator), keyMapper, valueMapper, true, CH_CONCURRENT_NOID);
    }

    public static <ELEM, KEY, VAL, MAP extends Map<KEY, VAL>> Collector<ELEM, ?, MAP> toMap(MAP map,
                                                                                            Function<ELEM, KEY> keyMapper,
                                                                                            Function<ELEM, VAL> valueMapper,
                                                                                            boolean allowKeyNull
    ) {
        return toMap(new Supplier<MAP>() {
            @Override
            public MAP get() {
                return map;
            }
        }, keyMapper, valueMapper, allowKeyNull, CH_ID);
    }

    public static <ELEM, KEY, VAL, MAP extends Map<KEY, VAL>> Collector<ELEM, ?, MAP> toMap(MAP map,
                                                                                            Function<ELEM, KEY> keyMapper,
                                                                                            Function<ELEM, VAL> valueMapper,
                                                                                            boolean allowKeyNull,
                                                                                            Set<Collector.Characteristics> characteristics
    ) {
        return toMap(new Supplier<MAP>() {
            @Override
            public MAP get() {
                return map;
            }
        }, keyMapper, valueMapper, allowKeyNull, characteristics);
    }

    public static <ELEM, KEY, VAL, MAP extends Map<KEY, VAL>> Collector<ELEM, ?, MAP> toMap(Supplier<MAP> mapSupplier,
                                                                                            Function<ELEM, KEY> keyMapper,
                                                                                            Function<ELEM, VAL> valueMapper,
                                                                                            boolean allowKeyNull) {
        return toMap(mapSupplier, keyMapper, valueMapper, allowKeyNull, CH_ID);
    }

    public static <ELEM, KEY, VAL, MAP extends Map<KEY, VAL>> Collector<ELEM, ?, MAP> toMap(Supplier<MAP> mapSupplier,
                                                                                            Function<ELEM, KEY> keyMapper,
                                                                                            Function<ELEM, VAL> valueMapper,
                                                                                            boolean allowKeyNull,
                                                                                            Set<Collector.Characteristics> characteristics
    ) {
        return new CollectorImpl<>(mapSupplier, new BiConsumer<MAP, ELEM>() {
            @Override
            public void accept(MAP map, ELEM elem) {
                KEY key = keyMapper.apply(elem);
                VAL val = valueMapper.apply(elem);
                if (key == null) {
                    if (allowKeyNull) {
                        map.put(key, val);
                    }
                } else {
                    map.put(key, val);
                }
            }
        },
                new BinaryOperator<MAP>() {
                    @Override
                    public MAP apply(MAP map1, MAP map2) {
                        for (Map.Entry<KEY, VAL> entry : map2.entrySet()) {
                            map1.put(entry.getKey(), entry.getValue());
                        }
                        return map1;
                    }
                },
                characteristics);
    }

}
