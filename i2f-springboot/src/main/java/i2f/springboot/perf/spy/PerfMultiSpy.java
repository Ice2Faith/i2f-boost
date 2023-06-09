package i2f.springboot.perf.spy;

import i2f.springboot.perf.data.PerfIndex;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:22
 * @desc
 */
public interface PerfMultiSpy {
    List<PerfIndex> collect();
}
