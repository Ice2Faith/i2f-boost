package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.spy.PerfSpy;
import org.springframework.stereotype.Component;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Component
public class GcCollectionCountSpy implements PerfSpy {
    private long lastCount = 0;

    @Override
    public String name() {
        return "Gc:CollectionCount";
    }

    @Override
    public double collect() {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        long count = 0;
        for (GarbageCollectorMXBean item : beans) {
            count += item.getCollectionCount();
        }
        long ret = count - lastCount;
        lastCount = count;
        return ret;
    }
}
