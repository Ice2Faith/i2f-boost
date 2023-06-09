package i2f.springboot.perf.context;

import i2f.springboot.perf.data.PerfIndex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

/**
 * @author Ice2Faith
 * @date 2023/6/8 8:44
 * @desc
 */
@Data
@NoArgsConstructor
public class PerfContext {
    private int maxCount = 300;
    private String title;
    private String yName;
    private String xName;
    private int count = 0;
    private long lastModify = -1;
    private LinkedList<PerfIndex> data = new LinkedList<>();

    public PerfContext(int maxCount, String title, String yName, String xName) {
        this.maxCount = maxCount;
        this.title = title;
        this.yName = yName;
        this.xName = xName;
    }

    public synchronized void add(PerfIndex item) {
        lastModify = System.currentTimeMillis();
        data.add(item);
        count++;
        if (count > maxCount) {
            data.removeFirst();
        }
    }
}
