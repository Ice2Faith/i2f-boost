package i2f.springboot.perf.spy.impl;

import i2f.core.os.perf.PerfUtil;
import i2f.core.os.perf.data.LinuxTop5Dto;
import i2f.springboot.perf.data.PerfIndex;
import i2f.springboot.perf.spy.PerfMultiSpy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/9 10:06
 * @desc
 */
@Component
public class LinuxTop5MultiSpy implements PerfMultiSpy {
    public static final String PREFIX = "Linux:Top5:";

    @Override
    public List<PerfIndex> collect() {
        List<PerfIndex> ret = new ArrayList<>();
        LinuxTop5Dto dto = null;
        try {
            dto = PerfUtil.getLinuxTop5();
        } catch (Exception e) {

        }
        if (dto == null) {
            return ret;
        }
        ret.add(new PerfIndex(PREFIX + "topUsers", dto.topUsers));
        ret.add(new PerfIndex(PREFIX + "loadAverage1", dto.loadAverage1));
        ret.add(new PerfIndex(PREFIX + "loadAverage2", dto.loadAverage2));
        ret.add(new PerfIndex(PREFIX + "loadAverage3", dto.loadAverage3));
        ret.add(new PerfIndex(PREFIX + "tasksTotal", dto.tasksTotal));
        ret.add(new PerfIndex(PREFIX + "tasksRunning", dto.tasksRunning));
        ret.add(new PerfIndex(PREFIX + "tasksSleeping", dto.tasksSleeping));
        ret.add(new PerfIndex(PREFIX + "tasksStopped", dto.tasksStopped));
        ret.add(new PerfIndex(PREFIX + "tasksZombie", dto.tasksZombie));
        ret.add(new PerfIndex(PREFIX + "cpuUs", dto.cpuUs));
        ret.add(new PerfIndex(PREFIX + "cpuSy", dto.cpuSy));
        ret.add(new PerfIndex(PREFIX + "cpuNi", dto.cpuNi));
        ret.add(new PerfIndex(PREFIX + "cpuId", dto.cpuId));
        ret.add(new PerfIndex(PREFIX + "cpuWa", dto.cpuWa));
        ret.add(new PerfIndex(PREFIX + "cpuHi", dto.cpuHi));
        ret.add(new PerfIndex(PREFIX + "cpuSi", dto.cpuSi));
        ret.add(new PerfIndex(PREFIX + "cpuSt", dto.cpuSt));
        ret.add(new PerfIndex(PREFIX + "kibMemTotalMb", dto.kibMemTotal / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibMemFreeMb", dto.kibMemFree / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibMemUsedMb", dto.kibMemUsed / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibMemUsedRate", dto.kibMemUsed * 100.0 / dto.kibMemTotal));
        ret.add(new PerfIndex(PREFIX + "kibMemBuffCacheMb", dto.kibMemBuffCache / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibSwapTotalMb", dto.kibSwapTotal / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibSwapFreeMb", dto.kibSwapFree / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibSwapUsedMb", dto.kibSwapUsed / 1024.0));
        ret.add(new PerfIndex(PREFIX + "kibSwapUsedRate", dto.kibSwapUsed * 100.0 / dto.kibSwapTotal));
        ret.add(new PerfIndex(PREFIX + "kibSwapAvailMemMb", dto.kibSwapAvailMem / 1024.0));


        return ret;
    }
}
