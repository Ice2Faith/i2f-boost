package i2f.core.thread.sync;


import i2f.core.type.tuple.impl.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author Ice2Faith
 * @date 2023/5/6 17:34
 * @desc 使用PV操作实现线程之间的依赖运行控制
 * 基于进程依赖的有向无环图DAG进行
 * 线程PV使用CountDownLatch实现
 * <p>
 * 一个线程运行的过程
 * 等待所有入度释放
 * 执行线程内容
 * 释放所有出度
 */
public class ProcessTaskRunner implements Callable<Map<String, Optional<?>>> {
    private Map<String, Callable<?>> taskNodes = new ConcurrentHashMap<>();
    private List<Tuple2<String, String>> taskLinks = new CopyOnWriteArrayList<>();

    private ExecutorService pool;

    public ProcessTaskRunner(ExecutorService pool) {
        this.pool = pool;
    }

    public ProcessTaskRunner addTask(String taskId, Callable<?> task) {
        taskNodes.put(taskId, task);
        return this;
    }


    public ProcessTaskRunner addLink(String beginTaskId, String endTaskId) {
        taskLinks.add(new Tuple2<>(beginTaskId, endTaskId));
        return this;
    }

    public static String makeTaskId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public synchronized ProcessTaskRunner addLinkTask(Callable<?> beginTask, Callable<?> endTask) {
        String beginTaskId = null;
        String endTaskId = null;
        for (Map.Entry<String, Callable<?>> entry : taskNodes.entrySet()) {
            if (entry.getValue().equals(beginTask)) {
                beginTaskId = entry.getKey();
            }
            if (entry.getValue().equals(endTask)) {
                endTaskId = entry.getKey();
            }
        }
        if (beginTaskId == null) {
            beginTaskId = makeTaskId();
        }
        if (endTaskId == null) {
            endTaskId = makeTaskId();
        }

        addTask(beginTaskId, beginTask);
        addTask(endTaskId, endTask);

        addLink(beginTaskId, endTaskId);
        return this;
    }

    @Override
    public Map<String, Optional<?>> call() throws Exception {
        Map<String, Optional<?>> retMap = new ConcurrentHashMap<>();

        // 信号量
        Map<String, CountDownLatch> signalMap = new ConcurrentHashMap<>();
        // 入度
        Map<String, List<String>> inputMap = new ConcurrentHashMap<>();
        // 出度
        Map<String, List<String>> outputMap = new ConcurrentHashMap<>();
        for (Tuple2<String, String> link : taskLinks) {
            String key = link.t1 + "/" + link.t2;
            signalMap.put(key, new CountDownLatch(1));

            if (!inputMap.containsKey(link.t2)) {
                inputMap.put(link.t2, new CopyOnWriteArrayList<>());
            }
            inputMap.get(link.t2).add(key);

            if (!outputMap.containsKey(link.t1)) {
                outputMap.put(link.t1, new CopyOnWriteArrayList<>());
            }
            outputMap.get(link.t1).add(key);
        }

        for (Map.Entry<String, Callable<?>> entry : taskNodes.entrySet()) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    Object ret = null;
                    String key = entry.getKey();

                    // P操作
                    if (inputMap.containsKey(key)) {
                        for (String link : inputMap.get(key)) {
                            CountDownLatch latch = signalMap.get(link);
                            try {
                                latch.await();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        Callable<?> task = entry.getValue();
                        ret = task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // V操作
                        if (outputMap.containsKey(key)) {
                            for (String link : outputMap.get(key)) {
                                CountDownLatch latch = signalMap.get(link);
                                latch.countDown();
                            }
                        }
                        retMap.put(key, Optional.ofNullable(ret));
                    }
                }
            });
        }

        return retMap;
    }
}
