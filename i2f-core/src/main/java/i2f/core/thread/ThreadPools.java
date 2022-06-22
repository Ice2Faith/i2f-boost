package i2f.core.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/6/22 8:55
 * @desc
 */
public class ThreadPools {
    private static final RejectedExecutionHandler defaultHandler =
            new ThreadPoolExecutor.AbortPolicy();

    public static class NamingThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NamingThreadFactory() {
            this(null, null, null);
        }

        public NamingThreadFactory(String poolPrefix, String threadPrefix) {
            this(null, poolPrefix, threadPrefix);
        }

        public NamingThreadFactory(ThreadGroup group, String poolPrefix, String threadPrefix) {
            if (group == null) {
                SecurityManager s = System.getSecurityManager();
                group = (s != null) ? s.getThreadGroup() :
                        Thread.currentThread().getThreadGroup();
            }

            String poolName = "pool";
            String threadName = "thread";
            if (poolPrefix != null) {
                poolName = poolPrefix;
            }
            if (threadPrefix != null) {
                threadName = threadPrefix;
            }

            this.group = group;
            this.namePrefix = poolName + "-" +
                    poolNumber.getAndIncrement() +
                    "-" + threadName + "-";

        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public static ExecutorService newThreadPool(int corePoolSize,
                                                int maximumPoolSize,
                                                long keepAliveTime,
                                                TimeUnit unit,
                                                BlockingQueue<Runnable> workQueue,
                                                ThreadFactory threadFactory,
                                                RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);
    }

    public static ExecutorService newNamingThreadPool(String poolName,
                                                      String threadName,
                                                      int corePoolSize,
                                                      int maximumPoolSize,
                                                      long keepAliveTime,
                                                      TimeUnit unit,
                                                      BlockingQueue<Runnable> workQueue,
                                                      RejectedExecutionHandler handler) {
        return newThreadPool(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                new NamingThreadFactory(null, poolName, threadName),
                handler);
    }

    public static ExecutorService newThreadPool(int corePoolSize,
                                                int maximumPoolSize,
                                                long keepAliveTime,
                                                TimeUnit unit,
                                                BlockingQueue<Runnable> workQueue,
                                                ThreadFactory threadFactory) {
        return newThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, defaultHandler);
    }

    public static ExecutorService newNamingThreadPool(String poolName,
                                                      String threadName,
                                                      int corePoolSize,
                                                      int maximumPoolSize,
                                                      long keepAliveTime,
                                                      TimeUnit unit,
                                                      BlockingQueue<Runnable> workQueue) {
        return newNamingThreadPool(poolName, threadName, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, defaultHandler);
    }

    public static ExecutorService newThreadPool(int corePoolSize,
                                                int maximumPoolSize,
                                                BlockingQueue<Runnable> workQueue,
                                                ThreadFactory threadFactory) {
        return newThreadPool(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue, threadFactory, defaultHandler);
    }

    public static ExecutorService newNamingThreadPool(String poolName,
                                                      String threadName,
                                                      int corePoolSize,
                                                      int maximumPoolSize,
                                                      BlockingQueue<Runnable> workQueue) {
        return newNamingThreadPool(poolName, threadName, corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue, defaultHandler);
    }


    public static ExecutorService newThreadPool(int corePoolSize, int maximumPoolSize) {
        return newThreadPool(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamingThreadFactory());
    }

    public static ExecutorService newNamingThreadPool(String poolName, String threadName, int corePoolSize, int maximumPoolSize) {
        return newThreadPool(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamingThreadFactory(poolName, threadName));
    }

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return newThreadPool(nThreads, nThreads);
    }

    public static ExecutorService newNamingFixedThreadPool(String poolName, String threadName, int nThreads) {
        return newNamingThreadPool(poolName, threadName, nThreads, nThreads);
    }

    public static ExecutorService newSingleThreadExecutor() {
        return newFixedThreadPool(1);
    }

    public static ExecutorService newNamingSingleThreadExecutor(String poolName, String threadName) {
        return newNamingFixedThreadPool(poolName,threadName,1);
    }

    public static ExecutorService newCachedThreadPool(int corePoolSize, int maximumPoolSize) {
        return newThreadPool(corePoolSize,maximumPoolSize,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),new NamingThreadFactory());
    }

    public static ExecutorService newNamingCachedThreadPool(String poolName, String threadName,int corePoolSize, int maximumPoolSize) {
        return newNamingThreadPool(poolName,threadName,corePoolSize,maximumPoolSize,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
    }

    public static ExecutorService newCachedThreadPool(int maximumPoolSize) {
        return newThreadPool(0,maximumPoolSize,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),new NamingThreadFactory());
    }

    public static ExecutorService newNamingCachedThreadPool(String poolName, String threadName,int maximumPoolSize) {
        return newNamingThreadPool(poolName,threadName,0,maximumPoolSize,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
    }

    public static ExecutorService newCachedThreadPool() {
        return newThreadPool(0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),new NamingThreadFactory());
    }

    public static ExecutorService newNamingCachedThreadPool(String poolName, String threadName) {
        return newNamingThreadPool(poolName,threadName,0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize){
        return new ScheduledThreadPoolExecutor(corePoolSize, new NamingThreadFactory());
    }

    public static ScheduledExecutorService newNamingScheduledThreadPool(String poolName, String threadName,int corePoolSize){
        return new ScheduledThreadPoolExecutor(corePoolSize, new NamingThreadFactory(poolName,threadName));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory());
    }

    public static ScheduledExecutorService newNamingSingleThreadScheduledExecutor(String poolName, String threadName) {
        return Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory(poolName,threadName));
    }
}
