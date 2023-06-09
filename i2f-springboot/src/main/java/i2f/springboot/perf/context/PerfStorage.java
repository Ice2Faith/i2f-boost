package i2f.springboot.perf.context;

import i2f.springboot.perf.data.PerfIndex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/6/8 8:53
 * @desc
 */
@Data
@NoArgsConstructor
public class PerfStorage {
    public static final String QPS_PREFIX = "QPS:";
    public static final String GLOBAL_QPS = QPS_PREFIX + "Global";

    public static final String RESPONSE_TIME_PREFIX = "RespTime:";

    public static PerfStorage STORE = new PerfStorage();

    public ConcurrentHashMap<String, PerfContext> indexesMap = new ConcurrentHashMap<>();
    public DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
    public ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    {
        pool.scheduleWithFixedDelay(() -> {
            Set<String> rm = new HashSet<>();
            long ts = System.currentTimeMillis();
            long rmts = ts - TimeUnit.SECONDS.toMillis(60);
            for (Map.Entry<String, PerfContext> entry : indexesMap.entrySet()) {
                long mod = entry.getValue().getLastModify();
                if (mod < rmts) {
                    rm.add(entry.getKey());
                }
            }
            for (String key : rm) {
                indexesMap.remove(key);
            }
        }, 0, 60, TimeUnit.SECONDS);
    }

    public void add(String title, double value) {
        if (!indexesMap.containsKey(title)) {
            indexesMap.put(title, new PerfContext(300, title, "指标值", "时间"));
        }
        indexesMap.get(title).add(new PerfIndex(fmt.format(LocalDateTime.now()), value));
    }
}
