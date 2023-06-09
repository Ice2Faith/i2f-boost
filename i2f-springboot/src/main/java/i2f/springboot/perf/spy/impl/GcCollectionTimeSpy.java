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
public class GcCollectionTimeSpy implements PerfSpy {
    private long lastTime = 0;

    @Override
    public String name() {
        return "Gc:CollectionTime";
    }

    @Override
    public double collect() {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        long time = 0;
        for (GarbageCollectorMXBean item : beans) {
            time += item.getCollectionTime();
        }
        long ret = time - lastTime;
        lastTime = time;
        return ret;
    }
}
