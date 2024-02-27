package i2f.stream.thread;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ForkJoinPool;

/**
 * @author ltb
 * @date 2022/11/11 10:57
 * @desc
 */
public class NamingForkJoinPool {
    public static synchronized ForkJoinPool getPool(String groupName,
                                                    String threadName) {
        return getPool(Math.min(MAX_CAP(), Runtime.getRuntime().availableProcessors()),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null,
                true,
                groupName,
                threadName);
    }

    public static synchronized ForkJoinPool getPool(int parallelism,
                                                    String groupName,
                                                    String threadName) {
        return getPool(parallelism,
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null,
                true,
                groupName,
                threadName);
    }

    public static synchronized ForkJoinPool getPool(int parallelism,
                                                    boolean asyncMode,
                                                    String groupName,
                                                    String threadName) {
        return getPool(parallelism,
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null,
                asyncMode,
                groupName,
                threadName);
    }

    public static synchronized ForkJoinPool getPool(int parallelism,
                                                    ForkJoinPool.ForkJoinWorkerThreadFactory factory,
                                                    Thread.UncaughtExceptionHandler handler,
                                                    boolean asyncMode,
                                                    String groupName,
                                                    String threadName) {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Constructor<ForkJoinPool>[] cons = (Constructor<ForkJoinPool>[]) clazz.getDeclaredConstructors();
            Constructor<ForkJoinPool> ins = null;
            for (Constructor<ForkJoinPool> con : cons) {
                if (Modifier.isPrivate(con.getModifiers()) && con.getParameterCount() == 5) {
                    ins = con;
                }
            }
            ins.setAccessible(true);
            ForkJoinPool pool = ins.newInstance(
                    checkParallelism(parallelism),
                    checkFactory(factory),
                    handler,
                    asyncMode ? FIFO_QUEUE() : LIFO_QUEUE(),
                    "fj-" + groupName + "-" + nextPoolId() + "-" + threadName + "-"
            );
            return pool;
        } catch (Exception e) {

        }
        return new ForkJoinPool(parallelism, factory, handler, asyncMode);
    }

    public static int FIFO_QUEUE() {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Field field = clazz.getDeclaredField("FIFO_QUEUE");
            field.setAccessible(true);
            int val = (Integer) field.get(null);
            return val;
        } catch (Exception e) {

        }
        return 1 << 16;
    }

    public static int LIFO_QUEUE() {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Field field = clazz.getDeclaredField("LIFO_QUEUE");
            field.setAccessible(true);
            int val = (Integer) field.get(null);
            return val;
        } catch (Exception e) {

        }
        return 0;
    }

    public static int MAX_CAP() {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Field field = clazz.getDeclaredField("MAX_CAP");
            field.setAccessible(true);
            int val = (Integer) field.get(null);
            return val;
        } catch (Exception e) {

        }
        return 0x7fff;
    }

    public static int checkParallelism(int parallelism) {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Method method = clazz.getDeclaredMethod("checkParallelism", int.class);
            method.setAccessible(true);
            return (Integer) method.invoke(null, parallelism);
        } catch (Exception e) {

        }
        return parallelism;
    }

    public static ForkJoinPool.ForkJoinWorkerThreadFactory checkFactory
            (ForkJoinPool.ForkJoinWorkerThreadFactory factory) {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Method method = clazz.getDeclaredMethod("checkFactory", ForkJoinPool.ForkJoinWorkerThreadFactory.class);
            method.setAccessible(true);
            return (ForkJoinPool.ForkJoinWorkerThreadFactory) method.invoke(null, factory);
        } catch (Exception e) {

        }
        return factory;
    }

    public static final synchronized int nextPoolId() {
        try {
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            Method method = clazz.getDeclaredMethod("nextPoolId");
            method.setAccessible(true);
            return (Integer) method.invoke(null);
        } catch (Exception e) {

        }
        return 1;
    }
}
