package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.spy.PerfSpy;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Component
public class MemHeapUsedMbSpy implements PerfSpy {
    @Override
    public String name() {
        return "Mem:Heap:UsedMb";
    }

    @Override
    public double collect() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024.0 / 1024.0;
    }
}
