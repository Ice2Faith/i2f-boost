package i2f.springboot.perf;

import i2f.springboot.perf.context.PerfStorage;
import i2f.springboot.perf.controller.PerfController;
import i2f.springboot.perf.exception.PerfExceptionCountSpy;
import i2f.springboot.perf.filter.PerfQpsFilter;
import i2f.springboot.perf.filter.PerfResponseTimeFilter;
import i2f.springboot.perf.listener.PerfListenerConfig;
import i2f.springboot.perf.spy.PerfIntervalSpy;
import i2f.springboot.perf.spy.impl.*;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/27 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        PerfConfig.class,
        PerfController.class,
        PerfExceptionCountSpy.class,
        PerfQpsFilter.class,
        PerfResponseTimeFilter.class,
        PerfListenerConfig.class,
        PerfIntervalSpy.class,
        ClassLoadedCountSpy.class,
        GcCollectionCountSpy.class,
        GcCollectionTimeSpy.class,
        LinuxDfMultiSpy.class,
        LinuxFreeMultiSpy.class,
        LinuxIostatMultiSpy.class,
        LinuxTop5MultiSpy.class,
        MemHeapCommittedMbSpy.class,
        MemHeapUsedMbSpy.class,
        MemHeapUsedRateSpy.class,
        MemNonHeapCommittedMbSpy.class,
        MemNonHeapUsedMbSpy.class,
        MemNonHeapUsedRateSpy.class,
        ObjectPendingFinalizationCountSpy.class,
        OsCpuProcessLoadSpy.class,
        OsCpuSystemLoadSpy.class,
        OsLoadAverageSpy.class,
        OsMemCommittedVirtualMbSpy.class,
        OsPhysicalMemFreeGbSpy.class,
        OsPhysicalMemUseRateSpy.class,
        OsSwapFreeGbSpy.class,
        OsSwapUseRateSpy.class,
        RuntimeMemUseRateSpy.class,
        ThreadCountSpy.class,
        ThreadDaemonCountSpy.class,
        ThreadPeakCountSpy.class,
        ThreadTimeMultiSpy.class,
})
public @interface EnablePerfConfig {

}

