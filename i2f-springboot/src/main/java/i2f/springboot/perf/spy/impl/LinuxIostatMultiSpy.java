package i2f.springboot.perf.spy.impl;

import i2f.core.perf.PerfUtil;
import i2f.core.perf.data.LinuxIostatDto;
import i2f.core.perf.data.LinuxIostatItemDto;
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
public class LinuxIostatMultiSpy implements PerfMultiSpy {
    public static final String PREFIX = "Linux:Iostat:";

    @Override
    public List<PerfIndex> collect() {
        List<PerfIndex> ret = new ArrayList<>();
        LinuxIostatDto dto = null;
        try {
            dto = PerfUtil.getLinuxIostatXk();
        } catch (Exception e) {

        }
        if (dto == null) {
            return ret;
        }
        if (dto.userPer != null) {
            ret.add(new PerfIndex(PREFIX + "userPer", dto.userPer));
        }
        if (dto.nicePer != null) {
            ret.add(new PerfIndex(PREFIX + "nicePer", dto.nicePer));
        }
        if (dto.systemPer != null) {
            ret.add(new PerfIndex(PREFIX + "systemPer", dto.systemPer));
        }
        if (dto.iowaitPer != null) {
            ret.add(new PerfIndex(PREFIX + "iowaitPer", dto.iowaitPer));
        }
        if (dto.stealPer != null) {
            ret.add(new PerfIndex(PREFIX + "stealPer", dto.stealPer));
        }
        if (dto.idlePer != null) {
            ret.add(new PerfIndex(PREFIX + "idlePer", dto.idlePer));
        }
        if (dto.items == null) {
            return ret;
        }
        for (LinuxIostatItemDto item : dto.items) {
            String prefix = PREFIX + "device:" + item.device + ":";

            if (item.rrqms != null) {
                ret.add(new PerfIndex(prefix + "rrqms", item.rrqms));
            }
            if (item.wrqms != null) {
                ret.add(new PerfIndex(prefix + "wrqms", item.wrqms));
            }
            if (item.rs != null) {
                ret.add(new PerfIndex(prefix + "rs", item.rs));
            }
            if (item.ws != null) {
                ret.add(new PerfIndex(prefix + "ws", item.ws));
            }
            if (item.rKbs != null) {
                ret.add(new PerfIndex(prefix + "rKbs", item.rKbs));
            }
            if (item.wKbs != null) {
                ret.add(new PerfIndex(prefix + "wKbs", item.wKbs));
            }
            if (item.avgRqSz != null) {
                ret.add(new PerfIndex(prefix + "avgRqSz", item.avgRqSz));
            }
            if (item.avgQuSz != null) {
                ret.add(new PerfIndex(prefix + "avgQuSz", item.avgQuSz));
            }
            if (item.await != null) {
                ret.add(new PerfIndex(prefix + "await", item.await));
            }
            if (item.rAwait != null) {
                ret.add(new PerfIndex(prefix + "rAwait", item.rAwait));
            }
            if (item.wAwait != null) {
                ret.add(new PerfIndex(prefix + "wAwait", item.wAwait));
            }
            if (item.svctm != null) {
                ret.add(new PerfIndex(prefix + "svctm", item.svctm));
            }
            if (item.utilPer != null) {
                ret.add(new PerfIndex(prefix + "utilPer", item.utilPer));
            }
        }

        return ret;
    }
}
