package i2f.stream.richable;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:33
 * @desc
 */
public class BaseRichStreamProcessor implements RichStreamProcessor {
    protected Map<String,Object> localContext;
    protected Map<String,Object> globalContext;

    protected boolean parallel;
    protected int parallelCount;
    protected ExecutorService pool;

    @Override
    public Map<String, Object> getLocalContext() {
        return this.localContext;
    }

    @Override
    public Map<String, Object> getGlobalContext() {
        return this.globalContext;
    }

    @Override
    public void setLocalContext(Map<String, Object> localContext) {
        this.localContext=localContext;
    }

    @Override
    public void setGlobalContext(Map<String, Object> globalContext) {
        this.globalContext = globalContext;
    }

    @Override
    public boolean isParallel() {
        return this.parallel;
    }

    @Override
    public void setParallel(boolean parallel) {
        this.parallel=parallel;
    }

    @Override
    public int parallelCount() {
        return this.parallelCount;
    }

    @Override
    public void setParallelCount(int count) {
        this.parallelCount=count;
    }

    @Override
    public ExecutorService getPool() {
        return this.pool;
    }

    @Override
    public void setPool(ExecutorService pool) {
        this.pool=pool;
    }

    @Override
    public void onBefore() {

    }

    @Override
    public void onAfter() {

    }
}
