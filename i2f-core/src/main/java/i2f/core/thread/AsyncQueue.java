package i2f.core.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/1/28 8:44
 * @desc 提供异步处理能力
 * ----------------------------------------
 * 一般用于将多线程中的任务合并到此队列中进行单线程串行队列化处理
 * 使用示例：
 * 使用instance方法实例化一个queue
 * <p>
 * 使用handle方法注册队列元素的消费者
 * <p>
 * 使用commit方法提交元素给队列
 */
public class AsyncQueue<E, Q extends BlockingQueue<E>> {

    private volatile AtomicBoolean handled = new AtomicBoolean(false);

    private volatile String name;

    private volatile Q queue;

    public AsyncQueue(String name, Q queue) {
        this.name = name;
        this.queue = queue;
    }

    public static <T> AsyncQueue<T, LinkedBlockingQueue<T>> instance(String name) {
        return new AsyncQueue<>(name, new LinkedBlockingQueue<>());
    }

    public synchronized void handle(Consumer<E> consumer) {
        if (handled.get()) {
            return;
        }
        handled.set(true);
        String threadName = "async-queue-handler";
        if (name != null) {
            threadName = name + "-" + "aqh";
        }
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    E elem = queue.take();
                    consumer.accept(elem);
                } catch (Throwable e) {
                    System.err.println("异步处理队里处理异常：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, threadName);
        thread.start();
    }

    public void submit(E elem) {
        try {
            queue.put(elem);
        } catch (Exception e) {
            System.err.println("异步处理队列提交异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

}
