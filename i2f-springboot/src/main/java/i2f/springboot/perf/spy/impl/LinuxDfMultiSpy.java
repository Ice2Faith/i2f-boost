package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.data.PerfIndex;
import i2f.springboot.perf.perf.PerfUtil;
import i2f.springboot.perf.perf.data.LinuxDfDto;
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
public class LinuxDfMultiSpy implements PerfMultiSpy {
    public static final String PREFIX = "Linux:Df:";

    @Override
    public List<PerfIndex> collect() {
        List<PerfIndex> ret = new ArrayList<>();
        List<LinuxDfDto> dtoList = null;
        try {
            dtoList = PerfUtil.getLinuxDf();
        } catch (Exception e) {

        }
        if (dtoList == null) {
            return ret;
        }
        for (LinuxDfDto item : dtoList) {
            String prefix = PREFIX + "fileSystem:" + item.fileSystem + ":mounted:" + item.mountedOn + ":";

            if (item.blocks1k != null) {
                ret.add(new PerfIndex(prefix + "blocksGb", item.blocks1k / 1024.0 / 1024.0));
            }
            if (item.used != null) {
                ret.add(new PerfIndex(prefix + "usedGb", item.used / 1024.0 / 1024.0));
            }
            if (item.available != null) {
                ret.add(new PerfIndex(prefix + "availableGb", item.available / 1024.0 / 1024.0));
            }
            if (item.usePercent != null) {
                ret.add(new PerfIndex(prefix + "usePercent", item.usePercent));
            }
        }

        return ret;
    }
}
