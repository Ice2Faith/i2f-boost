package i2f.core.streaming.impl;

import i2f.core.streaming.parallel.NamingForkJoinPool;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:04
 * @desc
 */
@Data
@NoArgsConstructor
public class StreamingContext {
    public static ExecutorService defaultPool = NamingForkJoinPool.getPool(Runtime.getRuntime().availableProcessors() * 2, "streaming", "thread");

    protected ExecutorService pool;
    protected ConcurrentHashMap<String, Optional<Object>> globalMap = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, Optional<Object>> processMap = new ConcurrentHashMap<>();
    protected boolean parallel = false;

    public <T> T globalGet(String key) {
        return (T) (globalMap.get(key).orElse(null));
    }

    public <T> T globalGet(String key, T defVal) {
        Object obj = globalGet(key);
        if (obj == null) {
            obj = defVal;
        }
        return (T) obj;
    }

    public void globalSet(String key, Object obj) {
        globalMap.put(key, Optional.ofNullable(obj));
    }

    public void globalRemove(String key) {
        globalMap.remove(key);
    }

    public boolean globalContains(String key) {
        return globalMap.containsKey(key);
    }

    public <T> T processGet(String key) {
        return (T) (processMap.get(key).orElse(null));
    }

    public <T> T processGet(String key, T defVal) {
        Object obj = processGet(key);
        if (obj == null) {
            obj = defVal;
        }
        return (T) obj;
    }

    public void processSet(String key, Object obj) {
        processMap.put(key, Optional.ofNullable(obj));
    }

    public void processRemove(String key) {
        processMap.remove(key);
    }

    public boolean processContains(String key) {
        return processMap.containsKey(key);
    }

}
