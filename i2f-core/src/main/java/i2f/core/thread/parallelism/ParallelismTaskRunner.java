package i2f.core.thread.parallelism;

/**
 * @author Ice2Faith
 * @date 2023/4/14 10:27
 * @desc
 */
public interface ParallelismTaskRunner<E> {
    void run(E elem, Object... args) throws Exception;
}