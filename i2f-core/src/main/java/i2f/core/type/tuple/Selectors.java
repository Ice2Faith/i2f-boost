package i2f.core.type.tuple;


import i2f.core.thread.LatchRunnable;
import i2f.core.type.tuple.impl.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/5/4 9:48
 * @desc
 */
public class Selectors {
    public static <T> Map<Integer, Optional<Object>> mapper(T obj, Function<T, ?>... mappers) {
        return mapper(null, obj, mappers);
    }

    public static <T> Map<Integer, Optional<Object>> mapper(ExecutorService pool, T obj, Function<T, ?>... mappers) {
        Map<Integer, Optional<Object>> ret = new ConcurrentHashMap<>();
        if (mappers.length == 0) {
            return ret;
        }
        CountDownLatch latch = new CountDownLatch(mappers.length);
        int cnt = 0;
        for (Function<T, ?> mapper : mappers) {
            final int retIdx = cnt;
            LatchRunnable task = new LatchRunnable(latch) {
                @Override
                public void doTask() throws Exception {
                    Object val = null;
                    if (mapper != null) {
                        val = mapper.apply(obj);
                    }
                    ret.put(retIdx, Optional.ofNullable(val));
                }
            };
            if (pool == null) {
                task.run();
            } else {
                pool.submit(task);
            }
            cnt++;
        }
        try {
            latch.await();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return ret;
    }


    public static <T, V1> Tuple1<V1> select(T obj, Function<T, V1> mapper1) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1);
        return Tuples.of((V1) map.get(0).orElse(null));
    }

    public static <T, V1, V2> Tuple2<V1, V2> select(T obj, Function<T, V1> mapper1,
                                                    Function<T, V2> mapper2
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null)
        );
    }

    public static <T, V1, V2, V3> Tuple3<V1, V2, V3> select(T obj, Function<T, V1> mapper1,
                                                            Function<T, V2> mapper2,
                                                            Function<T, V3> mapper3
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> select(T obj, Function<T, V1> mapper1,
                                                                    Function<T, V2> mapper2,
                                                                    Function<T, V3> mapper3,
                                                                    Function<T, V4> mapper4
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4, V5> Tuple5<V1, V2, V3, V4, V5> select(T obj, Function<T, V1> mapper1,
                                                                            Function<T, V2> mapper2,
                                                                            Function<T, V3> mapper3,
                                                                            Function<T, V4> mapper4,
                                                                            Function<T, V5> mapper5
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4,
                mapper5
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4, V5, V6> Tuple6<V1, V2, V3, V4, V5, V6> select(T obj, Function<T, V1> mapper1,
                                                                                    Function<T, V2> mapper2,
                                                                                    Function<T, V3> mapper3,
                                                                                    Function<T, V4> mapper4,
                                                                                    Function<T, V5> mapper5,
                                                                                    Function<T, V6> mapper6
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4,
                mapper5,
                mapper6
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null),
                (V6) map.get(5).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4, V5, V6, V7> Tuple7<V1, V2, V3, V4, V5, V6, V7> select(T obj, Function<T, V1> mapper1,
                                                                                            Function<T, V2> mapper2,
                                                                                            Function<T, V3> mapper3,
                                                                                            Function<T, V4> mapper4,
                                                                                            Function<T, V5> mapper5,
                                                                                            Function<T, V6> mapper6,
                                                                                            Function<T, V7> mapper7
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4,
                mapper5,
                mapper6,
                mapper7
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null),
                (V6) map.get(5).orElse(null),
                (V7) map.get(6).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4, V5, V6, V7, V8> Tuple8<V1, V2, V3, V4, V5, V6, V7, V8> select(T obj, Function<T, V1> mapper1,
                                                                                                    Function<T, V2> mapper2,
                                                                                                    Function<T, V3> mapper3,
                                                                                                    Function<T, V4> mapper4,
                                                                                                    Function<T, V5> mapper5,
                                                                                                    Function<T, V6> mapper6,
                                                                                                    Function<T, V7> mapper7,
                                                                                                    Function<T, V8> mapper8
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4,
                mapper5,
                mapper6,
                mapper7,
                mapper8
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null),
                (V6) map.get(5).orElse(null),
                (V7) map.get(6).orElse(null),
                (V8) map.get(7).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4, V5, V6, V7, V8, V9> Tuple9<V1, V2, V3, V4, V5, V6, V7, V8, V9> select(T obj, Function<T, V1> mapper1,
                                                                                                            Function<T, V2> mapper2,
                                                                                                            Function<T, V3> mapper3,
                                                                                                            Function<T, V4> mapper4,
                                                                                                            Function<T, V5> mapper5,
                                                                                                            Function<T, V6> mapper6,
                                                                                                            Function<T, V7> mapper7,
                                                                                                            Function<T, V8> mapper8,
                                                                                                            Function<T, V9> mapper9
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4,
                mapper5,
                mapper6,
                mapper7,
                mapper8,
                mapper9
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null),
                (V6) map.get(5).orElse(null),
                (V7) map.get(6).orElse(null),
                (V8) map.get(7).orElse(null),
                (V9) map.get(8).orElse(null)
        );
    }

    public static <T, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Tuple10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> select(T obj, Function<T, V1> mapper1,
                                                                                                                       Function<T, V2> mapper2,
                                                                                                                       Function<T, V3> mapper3,
                                                                                                                       Function<T, V4> mapper4,
                                                                                                                       Function<T, V5> mapper5,
                                                                                                                       Function<T, V6> mapper6,
                                                                                                                       Function<T, V7> mapper7,
                                                                                                                       Function<T, V8> mapper8,
                                                                                                                       Function<T, V9> mapper9,
                                                                                                                       Function<T, V10> mapper10
    ) {
        Map<Integer, Optional<Object>> map = mapper(obj, mapper1,
                mapper2,
                mapper3,
                mapper4,
                mapper5,
                mapper6,
                mapper7,
                mapper8,
                mapper9,
                mapper10
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null),
                (V6) map.get(5).orElse(null),
                (V7) map.get(6).orElse(null),
                (V8) map.get(7).orElse(null),
                (V9) map.get(8).orElse(null),
                (V10) map.get(9).orElse(null)
        );
    }
}
