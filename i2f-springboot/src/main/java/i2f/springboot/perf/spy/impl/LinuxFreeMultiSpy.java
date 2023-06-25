package i2f.springboot.perf.spy.impl;

import i2f.core.os.perf.PerfUtil;
import i2f.core.os.perf.data.LinuxFreeDto;
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
public class LinuxFreeMultiSpy implements PerfMultiSpy {
    public static final String PREFIX = "Linux:Free:";

    @Override
    public List<PerfIndex> collect() {
        List<PerfIndex> ret = new ArrayList<>();
        LinuxFreeDto dto = null;
        try {
            dto = PerfUtil.getLinuxFree();
        } catch (Exception e) {

        }
        if (dto == null) {
            return ret;
        }
        ret.add(new PerfIndex(PREFIX + "memTotalMb", dto.memTotal / 1024.0));
        ret.add(new PerfIndex(PREFIX + "memUsedMb", dto.memUsed / 1024.0));
        ret.add(new PerfIndex(PREFIX + "memUsedRate", dto.memUsed * 100.0 / dto.memTotal));
        ret.add(new PerfIndex(PREFIX + "memFreeMb", dto.memFree / 1024.0));
        ret.add(new PerfIndex(PREFIX + "memSharedMb", dto.memShared / 1024.0));
        ret.add(new PerfIndex(PREFIX + "memBuffCacheMb", dto.memBuffCache / 1024.0));
        ret.add(new PerfIndex(PREFIX + "memAvailableMb", dto.memAvailable / 1024.0));
        ret.add(new PerfIndex(PREFIX + "swapTotalMb", dto.swapTotal / 1024.0));
        ret.add(new PerfIndex(PREFIX + "swapUsedMb", dto.swapUsed / 1024.0));
        ret.add(new PerfIndex(PREFIX + "swapUsedRate", dto.swapUsed * 100.0 / dto.swapTotal));
        ret.add(new PerfIndex(PREFIX + "swapFreeMb", dto.swapFree / 1024.0));

        return ret;
    }
}
