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
public class ThreadCountSpy implements PerfSpy {
    @Override
    public String name() {
        return "Thread:count:all";
    }

    @Override
    public double collect() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }
}
