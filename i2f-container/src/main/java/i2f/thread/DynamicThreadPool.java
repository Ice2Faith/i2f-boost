package i2f.thread;

import com.google.common.util.concurrent.AtomicDouble;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2024/4/12 16:37
 * @desc 一个尽量占满CPU利用率的动态线程池实现
 * 内部线程复用，一个线程可能处理多个任务
 * 适用于提交的任务具有执行时间相似性的任务
 * 目标是尽量提升CPU利用率，避免因为IO、网络等造成的CPU利用率低的问题
 */
public class DynamicThreadPool {
    protected static class TaskInfo<T> {
        protected Callable<T> task;
        protected CompletableFuture<T> future;
        protected long submitTs;
        protected long triggerTs;
        protected long finishTs;
        protected Throwable except;

        public TaskInfo(Callable<T> task, CompletableFuture<T> future) {
            this.task = task;
            this.future = future;
            this.submitTs = System.currentTimeMillis();
        }
    }

    protected static class ThreadMeta {

        protected AtomicInteger taskCount = new AtomicInteger(0);

        protected AtomicLong initNanoSeconds = new AtomicLong(0);

        protected AtomicLong offsetNanoSeconds = new AtomicLong(0);

        protected AtomicLong offsetCpuNanoSeconds = new AtomicLong(0);
    }

    protected String poolName;
    protected static AtomicLong pollCount = new AtomicLong();

    public DynamicThreadPool() {
        long cnt = pollCount.incrementAndGet();
        poolName = "dynamic-pool-" + cnt;
    }

    public DynamicThreadPool(String poolName) {
        long cnt = pollCount.incrementAndGet();
        this.poolName = poolName + "-" + cnt;
    }

    protected AtomicLong threadCount = new AtomicLong(0);
    protected ConcurrentHashMap<String, Thread> runningThreads = new ConcurrentHashMap<>();
    protected LinkedBlockingQueue<TaskInfo<?>> taskQueue = new LinkedBlockingQueue<>();

    protected AtomicLong taskThreadIdleTs = new AtomicLong(30 * 1000);
    protected AtomicInteger maxThreadCount = new AtomicInteger(Runtime.getRuntime().availableProcessors());

    protected AtomicLong offsetWindowMillSeconds = new AtomicLong(10 * 1000 * 1000000);

    protected AtomicInteger maxThreadTaskCount = new AtomicInteger(0);
    protected SecureRandom random = new SecureRandom();

    protected ReentrantLock lock = new ReentrantLock();

    protected ConcurrentHashMap<String, ThreadMeta> threadMetaMap = new ConcurrentHashMap<>();

    public <T> Future<T> submit(Callable<T> task) throws InterruptedException {
        CompletableFuture<T> ret = new CompletableFuture<>();
        taskQueue.put(new TaskInfo<>(task, ret));
        addThread();
        return ret;
    }

    protected void onThreadNext(String threadId) {
        ThreadMeta meta = threadMetaMap.get(threadId);
        if (meta == null) {
            return;
        }
        long ts = System.currentTimeMillis();
        if (ts * 1000000 - meta.offsetNanoSeconds.get() < offsetWindowMillSeconds.get() * 1000000) {
            return;
        }
        Thread thread = runningThreads.get(threadId);
        if (thread == null) {
            return;
        }
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long threadCpuTime = threadMXBean.getThreadCpuTime(thread.getId());
        meta.offsetNanoSeconds.set(System.currentTimeMillis() * 1000000);
        meta.offsetCpuNanoSeconds.set(threadCpuTime);
    }

    AtomicDouble diffSum = new AtomicDouble(0);
    AtomicInteger diffCount = new AtomicInteger(0);

    protected void update(int size) {
        int old = maxThreadCount.get();
        maxThreadCount.set(size);
        if (old != size) {
            int max = maxThreadCount.get();
            int curr = runningThreads.size();
            double diffRate = (Math.abs(curr - max) * 1.0 / max * 100.0);
            diffSum.addAndGet(diffRate);
            diffCount.incrementAndGet();
            double avgRate = diffSum.get() / diffCount.get();
            double avgWaitTs = sumWaitTs.get() * 1.0 / processCount.get();
            System.out.println("task:" + taskQueue.size() +
                    ", old:" + old +
                    ", max:" + max +
                    ", curr:" + curr +
                    ", rate:" + String.format("%.2f", diffRate) +
                    ", avg:" + String.format("%.2f", avgRate) +
                    ", wait:" + String.format("%.2f", avgWaitTs));
        }
    }

    protected void addThread() {
        lock.lock();
        try {
            if (runningThreads.isEmpty()) {
                innerAddThread();
                return;
            }

            if (!runningThreads.isEmpty()) {
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

                int cnt = 0;
                double sumCpuRate = 0;
                for (Map.Entry<String, Thread> entry : runningThreads.entrySet()) {
                    ThreadMeta meta = threadMetaMap.get(entry.getKey());
                    if (meta == null) {
                        continue;
                    }

                    long cpuTime = threadMXBean.getThreadCpuTime(entry.getValue().getId());
                    cpuTime = cpuTime - meta.offsetCpuNanoSeconds.get();

                    Long initTime = meta.initNanoSeconds.get();
                    initTime = initTime - meta.offsetNanoSeconds.get();

                    long totalTime = (System.currentTimeMillis() * 1000000 - meta.offsetNanoSeconds.get()) - initTime;

                    double cpuRate = (cpuTime * 1.0 / totalTime * 100.0);

                    cnt++;
                    sumCpuRate += cpuRate;
                }

                if (cnt > 0) {

                    double avgCpuRate = sumCpuRate / cnt;

                    if (avgCpuRate > 0.0001) {
                        double mcnt = 100 / avgCpuRate;
                        mcnt = mcnt * 0.25 + maxThreadCount.get() * 0.75 + 0.5;
                        update(Math.max((int) mcnt, 1));
                    }
                }
            }

            if (runningThreads.size() > maxThreadCount.get()) {
                return;
            }

            int diff = maxThreadCount.get() / runningThreads.size();

            if (diff > 0) {
                for (int i = 0; i < diff; i++) {
                    innerAddThread();
                }
            } else {
                innerAddThread();
            }
        } finally {
            lock.unlock();
        }


    }

    protected AtomicLong sumWaitTs = new AtomicLong(0);
    protected AtomicLong processCount = new AtomicLong(0);

    protected synchronized void innerAddThread() {
        long cnt = threadCount.incrementAndGet();
        String threadId = "dynamic-thread-" + cnt;
        Thread thread = new Thread(() -> {
            long maxIdleTs = taskThreadIdleTs.get() + random.nextInt(Math.max((int) (taskThreadIdleTs.get() * 0.3), 100));
            double currIdleTs = maxIdleTs;
            ThreadMeta meta = threadMetaMap.get(threadId);
            long tid = Thread.currentThread().getId();
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            int maxDiffCount = Runtime.getRuntime().availableProcessors() * 4;
            try {
                int maxFailCount = 50;
                int failCount = 0;
                double yieldFactor = 1;
                int yieldCount = 0;
                long lastTs = -1;
                while (true) {
                    boolean isYield = false;

                    int runSize = runningThreads.size();
                    int maxCount = maxThreadCount.get();
                    double yieldRate = runSize * 1.0 / maxCount - 1.0;
                    double accRate = yieldCount * 0.01;
                    yieldRate += accRate;
                    double stdRate = (runSize - maxCount) * 1.0 / maxDiffCount;
                    if (stdRate > 0) {
                        yieldRate += stdRate;
                    }
                    if (yieldRate > 0) {
                        if (random.nextDouble() < yieldRate) {
                            isYield = true;
                        }
                    }

                    if (isYield) {
                        currIdleTs *= 0.9;
                        yieldCount++;
                    } else {
                        yieldCount = 0;
                        currIdleTs = maxIdleTs;
                    }

                    TaskInfo task = null;
                    if (!isYield) {
                        yieldFactor = 1;
                        task = taskQueue.poll();
                    } else {
                        yieldFactor *= 1.1;
                        if (yieldFactor > currIdleTs / 4.0) {
                            yieldFactor = currIdleTs / 4.0;
                        }
                        yieldFactor = Math.max(yieldFactor, 1);
                    }
                    if (task != null) {
                        failCount = 0;
                        lastTs = -1;
                        try {
                            meta.taskCount.incrementAndGet();
                            maxThreadTaskCount.updateAndGet((v) -> {
                                return Math.max(v, meta.taskCount.get());
                            });
                            task.triggerTs = System.currentTimeMillis();
                            long diffWaitTriggerTs = task.triggerTs - task.submitTs;
                            if (diffWaitTriggerTs > 1000) {
                                addThread();
                            }
                            sumWaitTs.addAndGet(diffWaitTriggerTs);
                            processCount.incrementAndGet();
                            Object result = task.task.call();
                            task.future.complete(result);
                        } catch (Throwable e) {
                            task.except = e;
                            task.future.completeExceptionally(e);
                            e.printStackTrace();
                        } finally {
                            task.finishTs = System.currentTimeMillis();
                            onThreadNext(threadId);
                            addThread();
                        }
                    } else {
                        if (lastTs > 0) {
                            long diff = System.currentTimeMillis() - lastTs;
                            if (diff > currIdleTs) {
                                break;
                            }
                        }
                        if (lastTs == -1) {
                            lastTs = System.currentTimeMillis();
                        }
                        failCount++;
                        if (failCount > maxFailCount) {
                            try {
                                int slpt = (int) yieldFactor;
                                Thread.sleep(isYield ? slpt + random.nextInt(Math.min(slpt / 4, 2)) : 0);
                            } catch (Exception e) {

                            }

                        }
                    }

                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                runningThreads.remove(threadId);
                threadMetaMap.remove(threadId);
                if (!taskQueue.isEmpty()) {
                    addThread();
                }
            }
        }, threadId);
        runningThreads.put(threadId, thread);
        ThreadMeta meta = new ThreadMeta();
        meta.offsetNanoSeconds.set(0);
        meta.initNanoSeconds.set(System.currentTimeMillis() * 1000000);
        threadMetaMap.put(threadId, meta);
        thread.start();
    }

}
