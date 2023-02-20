package i2f.extension.cron;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import i2f.core.thread.NamingThreadFactory;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2023/2/16 14:01
 * @desc 单体的cron任务执行器
 * 实例化执行器之后
 * 通过submit方法提交cron任务
 * 将会在cron任务执行的时间调用提交的任务
 * ------------------------------------
 * 运行逻辑
 * - 提交cron任务
 * - 分别保存到cronTaskMap和cronNewestTimeMap映射中
 * - 分别记录cron携带的最新执行时间和任务
 * - 定时扫描cronNewestTimeMap，如果当前时间大于最新时间
 * - 生成未来一段时间的几个任务，从cronTaskMap拷贝任务到等待任务executeTaskMap中
 * - 定时扫描executeTaskMap，将小于等于当前时间的任务列表拿出来
 * - 提交给执行任务的线程池executePool
 */
public class CronExecutor {
    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 1; i++) {
            testExecutor();
        }

        System.in.read();
    }

    public static void testExecutor() {
        SecureRandom rand = new SecureRandom();
        CronExecutor executor = new CronExecutor();
        for (int i = 1; i < 30; i++) {
            final int taskIter = i;
            executor.submit(CronType.QUARTZ, "0/" + i + " * * * * ?", () -> {
                try {
                    Thread.sleep(rand.nextInt(3000));
                } catch (Exception e) {

                }
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                System.out.println("exec" + taskIter + ":" + fmt.format(date));
            });
        }
    }

    // 实例自增长ID
    private static volatile AtomicInteger instanceId = new AtomicInteger(0);

    // 任务执行列表，指明了每个时刻等待执行的任务列表
    private volatile ConcurrentSkipListMap<Date, List<Runnable>> executeTaskMap = new ConcurrentSkipListMap<Date, List<Runnable>>();
    // 每个cron对应的任务，保存着每个cron对应要执行的任务
    private volatile ConcurrentHashMap<Cron, Runnable> cronTaskMap = new ConcurrentHashMap<Cron, Runnable>();
    // 每个cron最新的时间节点，以此为根据，生成未来cron时间点的几个任务cronTaskMap，放入executeTaskMap
    private volatile ConcurrentHashMap<Cron, Date> cronNewestTimeMap = new ConcurrentHashMap<>();

    // cron解析为任务执行时间的线程池，将cron解析为运行时间，放入等待任务列表 executeTaskMap
    private volatile ScheduledExecutorService cronPool;
    // 触发任务执行的线程池，将等待任务列表 executeTaskMap 的任务，触发放入 executePool
    private volatile ScheduledExecutorService triggerPool;

    // 执行任务的线程池
    private volatile ExecutorService executePool;

    // 任务等待链的互斥锁，锁住： executeTaskMap
    private volatile ReentrantLock taskLock = new ReentrantLock();
    // cron操作的互斥锁，锁住：cronTaskMap 和 cronNewestTimeMap
    private volatile ReentrantLock cronLock = new ReentrantLock();

    {
        int cnt = instanceId.incrementAndGet();

        String prefix = "cron-" + cnt;
        cronPool = Executors.newScheduledThreadPool(3, new NamingThreadFactory(prefix, "dispatcher"));
        triggerPool = Executors.newScheduledThreadPool(3, new NamingThreadFactory(prefix, "trigger"));
        executePool = Executors.newCachedThreadPool(new NamingThreadFactory(prefix, "executor"));
    }

    public CronExecutor() {
        initExecutor();
    }

    public CronExecutor(ExecutorService pool) {
        executePool = pool;
        initExecutor();
    }

    protected void initExecutor() {
        // 解析cron表达式任务到执行任务表
        cronPool.scheduleAtFixedRate(() -> {
            cronLock.lock();
            try {
                // 获取当前时间
                Date now = new Date(System.currentTimeMillis());
                // 遍历cron
                for (Cron cron : cronNewestTimeMap.keySet()) {
                    // 得到cron对应的最新任务时间
                    Date date = cronNewestTimeMap.get(cron);
                    // 判断是否需要生成最新任务时间
                    if (date.before(now)) {
                        // 生成未来10个任务时间点
                        List<Date> list = CronUtil.nextTimes(cron, 10);
                        taskLock.lock();
                        try {
                            // 将任务时间需要执行的任务，放入等待执行表
                            for (Date taskDate : list) {
                                if (!executeTaskMap.containsKey(taskDate)) {
                                    executeTaskMap.put(taskDate, new ArrayList<>());
                                }
                                executeTaskMap.get(taskDate).add(cronTaskMap.get(cron));
                                date = taskDate;
                            }
                        } finally {
                            taskLock.unlock();
                        }
                    }
                    // 更新最新任务时间
                    cronNewestTimeMap.put(cron, date);
                }
            } finally {
                cronLock.unlock();
            }
        }, 0, 300, TimeUnit.MILLISECONDS);


        // 触发任务
        triggerPool.scheduleAtFixedRate(() -> {

            taskLock.lock();
            try {
                // 获取当前时间
                Date now = new Date(System.currentTimeMillis());
                // 遍历等待执行的任务列表
                for (Date date : executeTaskMap.keySet()) {
                    // 如果任务时间小于当前时间，则需要立即触发执行
                    if (date.before(now)) {
                        // 将任务提交给执行线程池
                        List<Runnable> taskList = executeTaskMap.remove(date);
                        for (Runnable task : taskList) {
                            executePool.submit(task);
                        }
                    } else {
                        // 由于ConcurrentSkipListMap具有按键升序的能力
                        // 因此，当第一个键遍历到，是未来时间，则不需要执行
                        // 可以直接跳出
                        break;
                    }
                }
            } finally {
                taskLock.unlock();
            }
        }, 0, 300, TimeUnit.MILLISECONDS);
    }

    public void submit(CronType type, String cron, Runnable task) {
        Cron expression = CronUtil.getCron(type, cron);
        cronLock.lock();
        try {
            cronTaskMap.put(expression, task);
            cronNewestTimeMap.put(expression, new Date(System.currentTimeMillis()));
        } finally {
            cronLock.unlock();
        }
    }

    public void submit(Date date, Runnable task) {
        taskLock.lock();
        try {
            if (!executeTaskMap.containsKey(date)) {
                executeTaskMap.put(date, new ArrayList<>());
            }
            executeTaskMap.get(date).add(task);
        } finally {
            taskLock.unlock();
        }
    }

    public void submit(int delay, TimeUnit timeUnit, Runnable task) {
        long ts = System.currentTimeMillis() + timeUnit.toMillis(delay);
        Date date = new Date(ts);
        submit(date, task);
    }
}
