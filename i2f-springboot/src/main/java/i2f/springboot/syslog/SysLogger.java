package i2f.springboot.syslog;

import i2f.springboot.syslog.consts.LogLevel;
import i2f.springboot.syslog.data.SysLogDto;
import i2f.springboot.syslog.functional.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:15
 * @desc
 */
public interface SysLogger {
    // 基础部分和延迟赋值部分
    void log(SysLogDto dto, Consumer<SysLogDto> consumer);

    void log(LogLevel level, Consumer<SysLogDto> consumer);

    void log(SysLogDto log);

    void info(SysLogDto log);

    void warn(SysLogDto log);

    void error(SysLogDto log);

    void debug(SysLogDto log);

    void trace(SysLogDto log);

    void info(String label, Object... args);

    void warn(String label, Object... args);

    void error(String label, Object... args);

    void debug(String label, Object... args);

    void trace(String label, Object... args);


    void info(String label, Consumer<SysLogDto> consumer);

    void warn(String label, Consumer<SysLogDto> consumer);

    void error(String label, Consumer<SysLogDto> consumer);

    void debug(String label, Consumer<SysLogDto> consumer);

    void trace(String label, Consumer<SysLogDto> consumer);

    ////////////////////////////////////////////////////////////////////////
    void info(String label, Supplier<?> supplier);

    void warn(String label, Supplier<?> supplier);

    void error(String label, Supplier<?> supplier);

    void debug(String label, Supplier<?> supplier);

    void trace(String label, Supplier<?> supplier);


    ////////////////////////////////////////////////////////////////////////
    <T> void info(String label, PerfOneSupplier<?, T> src, T val);

    <T> void warn(String label, PerfOneSupplier<?, T> src, T val);

    <T> void error(String label, PerfOneSupplier<?, T> src, T val);

    <T> void debug(String label, PerfOneSupplier<?, T> src, T val);

    <T> void trace(String label, PerfOneSupplier<?, T> src, T val);

    ////////////////////////////////////////////////////////////////////////
    <V1, V2> void info(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2);

    <V1, V2> void warn(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2);

    <V1, V2> void error(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2);

    <V1, V2> void debug(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2);

    <V1, V2> void trace(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2);

    ////////////////////////////////////////////////////////////////////////
    <V1, V2, V3> void info(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3);

    <V1, V2, V3> void warn(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3);

    <V1, V2, V3> void error(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3);

    <V1, V2, V3> void debug(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3);

    <V1, V2, V3> void trace(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3);

    ////////////////////////////////////////////////////////////////////////
    void infoArgs(String label, PerfSupplier<?> src, Object... args);

    void warnArgs(String label, PerfSupplier<?> src, Object... args);

    void errorArgs(String label, PerfSupplier<?> src, Object... args);

    void debugArgs(String label, PerfSupplier<?> src, Object... args);

    void traceArgs(String label, PerfSupplier<?> src, Object... args);

    ////////////////////////////////////////////////////////////////////////
    <T extends Throwable> void infoEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args);

    <T extends Throwable> void warnEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args);

    <T extends Throwable> void errorEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args);

    <T extends Throwable> void debugEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args);

    <T extends Throwable> void traceEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args);
}
