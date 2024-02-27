package i2f.stream.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/8/5 16:31
 * @desc
 */
public class NamingThreadFactory implements ThreadFactory {
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
