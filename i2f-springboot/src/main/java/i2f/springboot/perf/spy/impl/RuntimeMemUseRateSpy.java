package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.spy.PerfSpy;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Component
public class RuntimeMemUseRateSpy implements PerfSpy {
    @Override
    public String name() {
        return "Runtime:Mem:UseRate";
    }

    @Override
    public double collect() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        double val = (totalMemory - freeMemory) * 100.0 / totalMemory;
        return Math.max(val, -1);
    }
}
