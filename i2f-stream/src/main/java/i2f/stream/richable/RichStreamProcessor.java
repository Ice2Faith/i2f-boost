package i2f.stream.richable;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:31
 * @desc
 */
public interface RichStreamProcessor {
    Map<String,Object> getLocalContext();
    Map<String,Object> getGlobalContext();

    void setLocalContext(Map<String,Object> localContext);
    void setGlobalContext(Map<String,Object> globalContext);

    boolean isParallel();
    void setParallel(boolean parallel);

    int parallelCount();
    void setParallelCount(int count);

    ExecutorService getPool();
    void setPool(ExecutorService pool);

    void onBefore();
    void onAfter();
}
