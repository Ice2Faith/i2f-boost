package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.spy.PerfSpy;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Component
public class OsSwapUseRateSpy implements PerfSpy {
    @Override
    public String name() {
        return "Os:Swap:UseRate";
    }

    @Override
    public double collect() {
        OperatingSystemMXBean osm = ManagementFactory.getOperatingSystemMXBean();
        if (osm instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sosm = (com.sun.management.OperatingSystemMXBean) osm;
            long total = sosm.getTotalSwapSpaceSize();
            long free = sosm.getFreeSwapSpaceSize();
            double val = (total - free) * 100.0 / total;
            return Math.max(val, -1);

        }
        return -1;
    }
}
