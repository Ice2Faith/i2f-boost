package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.spy.PerfSpy;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Component
public class MemNonHeapUsedRateSpy implements PerfSpy {
    @Override
    public String name() {
        return "Mem:NonHeap:UsedRate";
    }

    @Override
    public double collect() {
        MemoryUsage mem = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        double val = mem.getUsed() * 100.0 / mem.getMax();
        return Math.max(val, -1);
    }
}
