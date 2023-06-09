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
public class OsLoadAverageSpy implements PerfSpy {
    @Override
    public String name() {
        return "Os:LoadAverage";
    }

    @Override
    public double collect() {
        double val = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        if (val < 0) {
            return -1;
        }
        return val * 100.0;
    }
}
