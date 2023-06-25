package i2f.core.thread;


import i2f.core.type.tuple.Tuples;
import i2f.core.type.tuple.impl.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/1/31 11:15
 * @desc
 */
public class Asyncs {
    private static ForkJoinPool sharedPool = NamingForkJoinPool.getPool("async", "supplier");

    public static Map<Integer, Optional<Object>> async(Supplier<?>... suppliers) {
        return async(null, suppliers);
    }

    public static Map<Integer, Optional<Object>> async(ExecutorService pool, Supplier<?>... suppliers) {
        Map<Integer, Optional<Object>> ret = new ConcurrentHashMap<>();
        if (suppliers.length == 0) {
            return ret;
        }
        if (pool == null) {
            pool = sharedPool;
        }
        CountDownLatch latch = new CountDownLatch(suppliers.length);
        int cnt = 0;
        for (Supplier<?> supplier : suppliers) {
            final int retIdx = cnt;
            pool.submit(new LatchRunnable(latch) {
                @Override
                public void doTask() throws Exception {
                    if (supplier != null) {
                        Object obj = supplier.get();
                        ret.put(retIdx, Optional.ofNullable(obj));
                    }
                }
            });
            cnt++;
        }
        try {
            latch.await();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return ret;
    }

    public static <V1> Tuple1<V1> promise(Supplier<V1> supplier1) {
        Map<Integer, Optional<Object>> map = async(supplier1);
        return Tuples.of((V1) map.get(0).orElse(null));
    }

    public static <V1, V2> Tuple2<V1, V2> promise(Supplier<V1> supplier1,
                                                  Supplier<V2> supplier2
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null)
        );
    }

    public static <V1, V2, V3> Tuple3<V1, V2, V3> promise(Supplier<V1> supplier1,
                                                          Supplier<V2> supplier2,
                                                          Supplier<V3> supplier3
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null)
        );
    }

    public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> promise(Supplier<V1> supplier1,
                                                                  Supplier<V2> supplier2,
                                                                  Supplier<V3> supplier3,
                                                                  Supplier<V4> supplier4
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null)
        );
    }

    public static <V1, V2, V3, V4, V5> Tuple5<V1, V2, V3, V4, V5> promise(Supplier<V1> supplier1,
                                                                          Supplier<V2> supplier2,
                                                                          Supplier<V3> supplier3,
                                                                          Supplier<V4> supplier4,
                                                                          Supplier<V5> supplier5
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4,
                supplier5
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null)
        );
    }

    public static <V1, V2, V3, V4, V5, V6> Tuple6<V1, V2, V3, V4, V5, V6> promise(Supplier<V1> supplier1,
                                                                                  Supplier<V2> supplier2,
                                                                                  Supplier<V3> supplier3,
                                                                                  Supplier<V4> supplier4,
                                                                                  Supplier<V5> supplier5,
                                                                                  Supplier<V6> supplier6
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4,
                supplier5,
                supplier6
        );
        return Tuples.of((V1) map.get(0).orElse(null),
                (V2) map.get(1).orElse(null),
                (V3) map.get(2).orElse(null),
                (V4) map.get(3).orElse(null),
                (V5) map.get(4).orElse(null),
                (V6) map.get(5).orElse(null)
        );
    }

    public static <V1, V2, V3, V4, V5, V6, V7> Tuple7<V1, V2, V3, V4, V5, V6, V7> promise(Supplier<V1> supplier1,
                                                                                          Supplier<V2> supplier2,
                                                                                          Supplier<V3> supplier3,
                                                                                          Supplier<V4> supplier4,
                                                                                          Supplier<V5> supplier5,
                                                                                          Supplier<V6> supplier6,
                                                                                          Supplier<V7> supplier7
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4,
                supplier5,
                supplier6,
                supplier7
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

    public static <V1, V2, V3, V4, V5, V6, V7, V8> Tuple8<V1, V2, V3, V4, V5, V6, V7, V8> promise(Supplier<V1> supplier1,
                                                                                                  Supplier<V2> supplier2,
                                                                                                  Supplier<V3> supplier3,
                                                                                                  Supplier<V4> supplier4,
                                                                                                  Supplier<V5> supplier5,
                                                                                                  Supplier<V6> supplier6,
                                                                                                  Supplier<V7> supplier7,
                                                                                                  Supplier<V8> supplier8
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4,
                supplier5,
                supplier6,
                supplier7,
                supplier8
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

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Tuple9<V1, V2, V3, V4, V5, V6, V7, V8, V9> promise(Supplier<V1> supplier1,
                                                                                                          Supplier<V2> supplier2,
                                                                                                          Supplier<V3> supplier3,
                                                                                                          Supplier<V4> supplier4,
                                                                                                          Supplier<V5> supplier5,
                                                                                                          Supplier<V6> supplier6,
                                                                                                          Supplier<V7> supplier7,
                                                                                                          Supplier<V8> supplier8,
                                                                                                          Supplier<V9> supplier9
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4,
                supplier5,
                supplier6,
                supplier7,
                supplier8,
                supplier9
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

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Tuple10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> promise(Supplier<V1> supplier1,
                                                                                                                     Supplier<V2> supplier2,
                                                                                                                     Supplier<V3> supplier3,
                                                                                                                     Supplier<V4> supplier4,
                                                                                                                     Supplier<V5> supplier5,
                                                                                                                     Supplier<V6> supplier6,
                                                                                                                     Supplier<V7> supplier7,
                                                                                                                     Supplier<V8> supplier8,
                                                                                                                     Supplier<V9> supplier9,
                                                                                                                     Supplier<V10> supplier10
    ) {
        Map<Integer, Optional<Object>> map = async(supplier1,
                supplier2,
                supplier3,
                supplier4,
                supplier5,
                supplier6,
                supplier7,
                supplier8,
                supplier9,
                supplier10
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
