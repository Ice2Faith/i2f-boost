package i2f.core.thread;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

public class AsyncMessageAwaiter implements Closeable {

    public AsyncMessageAwaiter() {

    }

    public AsyncMessageAwaiter(long keyMaxAliveTimeSeconds) {
        this.keyMaxAliveTimeSeconds = keyMaxAliveTimeSeconds;
    }

    /**
     * 异步消息等待Mapping
     */
    private volatile ConcurrentHashMap<Awaiter, Object> msgMapping = new ConcurrentHashMap<>();
    /**
     * 超时清理Mapping
     * 防止Map由于引用导致不断增大，最终OOM
     */
    private volatile ConcurrentHashMap<Awaiter, ScheduledFuture<?>> cleanMapping = new ConcurrentHashMap<>();
    /**
     * 定时清除使用的线程池
     */
    private volatile ScheduledExecutorService cleanPool = Executors.newScheduledThreadPool(5);
    /**
     * 定时清除的最大键存活时长
     */
    private volatile long keyMaxAliveTimeSeconds = 15 * 60;

    /**
     * 由于Map不支持存储null而引入特殊类型
     */
    private static class NullRef {

    }

    private static NullRef NULL_OBJ = new NullRef();

    /**
     * 配合promise实现异步调用等待的接口
     */
    @FunctionalInterface
    public interface PromiseTask {
        void run(AsyncMessageAwaiter awaiter, Awaiter lock);
    }

    /**
     * 等待器的定义
     * 为了防止外部直接使用
     */
    public interface Awaiter {
        void then();

        void await() throws InterruptedException;

        void await(long timeout, TimeUnit unit) throws InterruptedException;
    }

    /**
     * 使用CountDownLatch作为等待器
     */
    public static class CountDownLatchAwaiter implements Awaiter {
        private Object tag;
        private volatile CountDownLatch latch = new CountDownLatch(1);

        public CountDownLatchAwaiter() {

        }

        public CountDownLatchAwaiter(Object tag) {
            this.tag = tag;
        }

        @Override
        public void then() {
            latch.countDown();
        }

        @Override
        public void await() throws InterruptedException {
            latch.await();
        }

        @Override
        public void await(long timeout, TimeUnit unit) throws InterruptedException {
            latch.await(timeout, unit);
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }

    /**
     * 异步设置结果的任务
     *
     * @param task
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> T promise(PromiseTask task) throws InterruptedException {
        Awaiter lock = async();
        task.run(this, lock);
        return await(lock);
    }

    @Override
    public void close() throws IOException {
        shutdown();
    }

    public void shutdown() {
        cleanPool.shutdown();
    }

    /**
     * 内部使用的设置键值方法
     *
     * @param lock
     * @param obj
     */
    protected void put(Awaiter lock, Object obj) {
        if (obj == null) {
            obj = NULL_OBJ;
        }
        msgMapping.put(lock, obj);
        ScheduledFuture<?> future = cleanPool.schedule(() -> {
            msgMapping.remove(lock);
            cleanMapping.remove(lock);
        }, keyMaxAliveTimeSeconds, TimeUnit.SECONDS);
        ScheduledFuture<?> old = cleanMapping.get(lock);
        if (old != null) {
            cleanMapping.remove(lock);
            old.cancel(true);
        }
        cleanMapping.put(lock, future);
    }

    public Awaiter async() {
        Awaiter lock = new CountDownLatchAwaiter();
        put(lock, NULL_OBJ);
        return lock;
    }

    /**
     * 开启一个异步调用，获得一个等待器
     *
     * @return
     */
    public Awaiter async(Object tag) {
        Awaiter lock = new CountDownLatchAwaiter(tag);
        put(lock, NULL_OBJ);
        return lock;
    }

    /**
     * 设置等待器的结果，让等待器结束等待
     *
     * @param lock
     * @param value
     * @return
     */
    public Awaiter then(Awaiter lock, Object value) {
        put(lock, value);
        lock.then();
        return lock;
    }

    /**
     * 等待等待器的结果，一直等待
     *
     * @param lock
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> T await(Awaiter lock) throws InterruptedException {
        return await(lock, -1, TimeUnit.MILLISECONDS);
    }

    /**
     * 等待结果，直到超时时间到达
     *
     * @param lock
     * @param timeoutMillis
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> T await(Awaiter lock, long timeoutMillis) throws InterruptedException {
        return await(lock, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 等待结果，直到超时时间到达
     *
     * @param lock
     * @param timeout
     * @param timeUnit
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> T await(Awaiter lock, long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (timeout < 0) {
            lock.await();
        } else {
            lock.await(timeout, timeUnit);
        }
        Object ret = msgMapping.remove(lock);
        ScheduledFuture<?> future = cleanMapping.get(lock);
        if (future != null) {
            cleanMapping.remove(lock);
            future.cancel(true);
        }
        if (ret instanceof NullRef) {
            ret = null;
        }
        return (T) ret;
    }
}
