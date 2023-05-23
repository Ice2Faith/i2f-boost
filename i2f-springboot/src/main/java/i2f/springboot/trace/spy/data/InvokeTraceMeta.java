package i2f.springboot.trace.spy.data;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2023/5/23 11:17
 * @desc
 */
public class InvokeTraceMeta {
    public InvokeType type;
    public LocalDateTime time;

    public String remark;

    public Class clazz;
    public Method method;
    public String[] paramNames;
    public Object[] args;
    public Object ret;
    public Throwable ex;

    public StackTraceElement stack;

    public long beginTs;
    public long endTs;
    public long useTs;

    public String batchId;
    public int level;
    public int step;

    public long threadId;
    public String threadName;
    public String threadGroupName;

    public long memFree;
    public long memTotal;
    public long memMax;
    public double memUseRate;

    public InvokeTraceMeta assignThreadInfo(Thread thread) {
        this.threadId = thread.getId();
        this.threadName = thread.getName();
        this.threadGroupName = thread.getThreadGroup().getName();
        return this;
    }

    public InvokeTraceMeta assignRuntimeInfo(Runtime runtime) {
        this.memFree = runtime.freeMemory();
        this.memTotal = runtime.totalMemory();
        this.memMax = runtime.maxMemory();
        this.memUseRate = (((int) ((1.0 - (this.memFree * 1.0 / this.memTotal)) * 10000)) / 100.0);
        return this;
    }
}