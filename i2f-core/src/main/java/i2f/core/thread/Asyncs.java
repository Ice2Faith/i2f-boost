package i2f.core.thread;


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
}
