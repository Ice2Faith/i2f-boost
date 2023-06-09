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
public class OsPhysicalMemFreeGbSpy implements PerfSpy {
    @Override
    public String name() {
        return "Os:PhysicalMem:FreeGb";
    }

    @Override
    public double collect() {
        OperatingSystemMXBean osm = ManagementFactory.getOperatingSystemMXBean();
        if (osm instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sosm = (com.sun.management.OperatingSystemMXBean) osm;
            return sosm.getFreePhysicalMemorySize() / 1024.0 / 1024.0 / 1024.0;

        }
        return -1;
    }
}
