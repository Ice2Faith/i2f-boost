package i2f.springboot.syslog.impl;

import i2f.springboot.syslog.SysLogger;
import i2f.springboot.syslog.consts.LogLevel;
import i2f.springboot.syslog.consts.LogType;
import i2f.springboot.syslog.data.SysLogDto;
import i2f.springboot.syslog.functional.*;
import i2f.springboot.syslog.util.SysLogUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:30
 * @desc
 */
public abstract class AbsSysLogger implements SysLogger {

    @Override
    public void info(SysLogDto log) {
        log.setLogLevel(LogLevel.INFO.code());
        log(log);
    }

    @Override
    public void warn(SysLogDto log) {
        log.setLogLevel(LogLevel.WARN.code());
        log(log);
    }

    @Override
    public void error(SysLogDto log) {
        log.setLogLevel(LogLevel.ERROR.code());
        log(log);
    }

    @Override
    public void debug(SysLogDto log) {
        log.setLogLevel(LogLevel.DEBUG.code());
        log(log);
    }

    @Override
    public void trace(SysLogDto log) {
        log.setLogLevel(LogLevel.TRACE.code());
        log(log);
    }


    public void logProxy(LogLevel level, String label, Object... args) {
        log(level, (dto) -> {
            dto.setLogType(LogType.OUTPUT.code());
            dto.setSrcLabel(label);
            dto.setLogLevel(level.code());
            SysLogUtil.fillLogContentByArgs(dto, args);
            SysLogUtil.fillSystemModuleByEnvironment(dto);
        });
    }

    @Override
    public void info(String label, Object... args) {
        logProxy(LogLevel.INFO, label, args);
    }

    @Override
    public void warn(String label, Object... args) {
        logProxy(LogLevel.WARN, label, args);
    }

    @Override
    public void error(String label, Object... args) {
        logProxy(LogLevel.ERROR, label, args);
    }

    @Override
    public void debug(String label, Object... args) {
        logProxy(LogLevel.DEBUG, label, args);
    }

    @Override
    public void trace(String label, Object... args) {
        logProxy(LogLevel.TRACE, label, args);
    }

    public void logProxy(LogLevel level, String label, Consumer<SysLogDto> consumer) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            consumer.accept(dto);
        });
    }

    @Override
    public void info(String label, Consumer<SysLogDto> consumer) {
        logProxy(LogLevel.INFO, label, consumer);
    }

    @Override
    public void warn(String label, Consumer<SysLogDto> consumer) {
        logProxy(LogLevel.WARN, label, consumer);
    }

    @Override
    public void error(String label, Consumer<SysLogDto> consumer) {
        logProxy(LogLevel.ERROR, label, consumer);
    }

    @Override
    public void debug(String label, Consumer<SysLogDto> consumer) {
        logProxy(LogLevel.DEBUG, label, consumer);
    }

    @Override
    public void trace(String label, Consumer<SysLogDto> consumer) {
        logProxy(LogLevel.TRACE, label, consumer);
    }

    public void logProxy(LogLevel level, String label, Supplier<?> supplier) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            Object val = supplier.get();
            SysLogUtil.fillLogContentByArgs(dto, val);
        });
    }

    @Override
    public void info(String label, Supplier<?> supplier) {
        logProxy(LogLevel.INFO, label, supplier);
    }

    @Override
    public void warn(String label, Supplier<?> supplier) {
        logProxy(LogLevel.WARN, label, supplier);
    }

    @Override
    public void error(String label, Supplier<?> supplier) {
        logProxy(LogLevel.ERROR, label, supplier);
    }

    @Override
    public void debug(String label, Supplier<?> supplier) {
        logProxy(LogLevel.DEBUG, label, supplier);
    }

    @Override
    public void trace(String label, Supplier<?> supplier) {
        logProxy(LogLevel.TRACE, label, supplier);
    }

    public <T> void logProxy(LogLevel level, String label, PerfOneSupplier<?, T> src, T val) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            Object obj = src.get(val);
            SysLogUtil.fillLogContentByArgs(dto, obj);
        });
    }

    @Override
    public <T> void info(String label, PerfOneSupplier<?, T> src, T val) {
        logProxy(LogLevel.INFO, label, src, val);
    }

    @Override
    public <T> void warn(String label, PerfOneSupplier<?, T> src, T val) {
        logProxy(LogLevel.WARN, label, src, val);
    }

    @Override
    public <T> void error(String label, PerfOneSupplier<?, T> src, T val) {
        logProxy(LogLevel.ERROR, label, src, val);
    }

    @Override
    public <T> void debug(String label, PerfOneSupplier<?, T> src, T val) {
        logProxy(LogLevel.DEBUG, label, src, val);
    }

    @Override
    public <T> void trace(String label, PerfOneSupplier<?, T> src, T val) {
        logProxy(LogLevel.TRACE, label, src, val);
    }

    public <V1, V2> void logProxy(LogLevel level, String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            Object obj = src.get(v1, v2);
            SysLogUtil.fillLogContentByArgs(dto, obj);
        });
    }

    @Override
    public <V1, V2> void info(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2) {
        logProxy(LogLevel.INFO, label, src, v1, v2);
    }

    @Override
    public <V1, V2> void warn(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2) {
        logProxy(LogLevel.WARN, label, src, v1, v2);
    }

    @Override
    public <V1, V2> void error(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2) {
        logProxy(LogLevel.ERROR, label, src, v1, v2);
    }

    @Override
    public <V1, V2> void debug(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2) {
        logProxy(LogLevel.DEBUG, label, src, v1, v2);
    }

    @Override
    public <V1, V2> void trace(String label, PerfTwoSupplier<?, V1, V2> src, V1 v1, V2 v2) {
        logProxy(LogLevel.TRACE, label, src, v1, v2);
    }

    public <V1, V2, V3> void logProxy(LogLevel level, String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            Object obj = src.get(v1, v2, v3);
            SysLogUtil.fillLogContentByArgs(dto, obj);
        });
    }

    @Override
    public <V1, V2, V3> void info(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3) {
        logProxy(LogLevel.INFO, label, src, v1, v2, v3);
    }

    @Override
    public <V1, V2, V3> void warn(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3) {
        logProxy(LogLevel.WARN, label, src, v1, v2, v3);
    }

    @Override
    public <V1, V2, V3> void error(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3) {
        logProxy(LogLevel.ERROR, label, src, v1, v2, v3);
    }

    @Override
    public <V1, V2, V3> void debug(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3) {
        logProxy(LogLevel.DEBUG, label, src, v1, v2, v3);
    }

    @Override
    public <V1, V2, V3> void trace(String label, PerfThreeSupplier<?, V1, V2, V3> src, V1 v1, V2 v2, V3 v3) {
        logProxy(LogLevel.TRACE, label, src, v1, v2, v3);
    }

    public void logProxyArgs(LogLevel level, String label, PerfSupplier<?> src, Object... args) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            Object obj = src.get(args);
            SysLogUtil.fillLogContentByArgs(dto, obj);
        });
    }

    @Override
    public void infoArgs(String label, PerfSupplier<?> src, Object... args) {
        logProxyArgs(LogLevel.INFO, label, src, args);
    }

    @Override
    public void warnArgs(String label, PerfSupplier<?> src, Object... args) {
        logProxyArgs(LogLevel.WARN, label, src, args);
    }

    @Override
    public void errorArgs(String label, PerfSupplier<?> src, Object... args) {
        logProxyArgs(LogLevel.ERROR, label, src, args);
    }

    @Override
    public void debugArgs(String label, PerfSupplier<?> src, Object... args) {
        logProxyArgs(LogLevel.DEBUG, label, src, args);
    }

    @Override
    public void traceArgs(String label, PerfSupplier<?> src, Object... args) {
        logProxyArgs(LogLevel.TRACE, label, src, args);
    }

    public <T extends Throwable> void logProxyEx(LogLevel level, String label, PerfExceptionSupplier<?, T> src, T e, Object... args) {
        log(level, (dto) -> {
            dto.setSrcLabel(label);
            Object obj = src.get(e, args);
            SysLogUtil.fillLogContentByArgs(dto, obj);
        });
    }

    @Override
    public <T extends Throwable> void infoEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args) {
        logProxyEx(LogLevel.INFO, label, src, e, args);
    }

    @Override
    public <T extends Throwable> void warnEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args) {
        logProxyEx(LogLevel.WARN, label, src, e, args);
    }

    @Override
    public <T extends Throwable> void errorEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args) {
        logProxyEx(LogLevel.ERROR, label, src, e, args);
    }

    @Override
    public <T extends Throwable> void debugEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args) {
        logProxyEx(LogLevel.DEBUG, label, src, e, args);
    }

    @Override
    public <T extends Throwable> void traceEx(String label, PerfExceptionSupplier<?, T> src, T e, Object... args) {
        logProxyEx(LogLevel.TRACE, label, src, e, args);
    }
}
