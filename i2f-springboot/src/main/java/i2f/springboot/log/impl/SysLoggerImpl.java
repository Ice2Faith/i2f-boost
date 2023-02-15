package i2f.springboot.log.impl;

import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.consts.LogLevel;
import i2f.springboot.log.core.SpringContextProvider;
import i2f.springboot.log.data.SysLogDto;
import i2f.springboot.log.handler.SysLogHandler;
import i2f.springboot.log.util.SysLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:18
 * @desc
 */
@SysLog(value = false)
@Slf4j
public class SysLoggerImpl extends AbsSysLogger {
    private String location;

    private static LinkedBlockingQueue<SysLogDto> logQueue = new LinkedBlockingQueue<>();

    static {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    if (!SpringContextProvider.inited.get()) {
                        Thread.sleep(2);
                        continue;
                    }
                    Map<String, SysLogHandler> handlers = SpringContextProvider.getBeansByType(SysLogHandler.class);
                    if (!handlers.isEmpty()) {
                        SysLogDto dto = logQueue.take();
                        SysLogUtil.fillSystemModuleByEnvironment(dto);
                        for (String name : handlers.keySet()) {
                            SysLogHandler handler = handlers.get(name);
                            try {
                                handler.handle(dto);
                            } catch (Throwable e) {
                                log.warn("日志处理器(" + name + ":" + handler.getClass().getSimpleName() + ")处理异常" + e.getMessage(), e);
                            }
                        }
                    } else {
                        Thread.sleep(2);
                    }
                } catch (Throwable e) {
                    log.warn("系统日志处理异常：" + e.getMessage(), e);
                }
            }
        }, "sys-logger");
        thread.start();
    }


    public SysLoggerImpl(String location) {
        this.location = location;
    }

    @Override
    public void log(LogLevel level, Consumer<SysLogDto> consumer) {
        SysLogDto dto = SysLogUtil.instanceLog();
        if (level != null) {
            dto.setLogLevel(level.code());
        }
        log(dto, consumer);
    }

    @Override
    public void log(SysLogDto dto, Consumer<SysLogDto> consumer) {
        try {
            if (StringUtils.isEmpty(dto.getLogLocation())) {
                dto.setLogLocation(location);
            }
            if (predicateOutputLog(dto)) {
                SysLogUtil.fillSystemModuleByEnvironment(dto);
                if (consumer != null) {
                    consumer.accept(dto);
                }
                logQueue.put(dto);
            }
        } catch (Throwable e) {
            log.warn("日志输出异常：" + e.getMessage(), e);
        }
    }

    @Override
    public void log(SysLogDto dto) {
        log(dto, (Consumer<SysLogDto>) null);
    }

    public boolean predicateOutputLog(SysLogDto dto) {
        Logger logger = LoggerFactory.getLogger(dto.getLogLocation());
        Integer logLevel = dto.getLogLevel();
        if (logLevel == null) {
            logLevel = LogLevel.INFO.code();
        }
        if (LogLevel.INFO.code() == logLevel && logger.isInfoEnabled()) {
            return true;
        }
        if (LogLevel.WARN.code() == logLevel && logger.isWarnEnabled()) {
            return true;
        }
        if (LogLevel.ERROR.code() == logLevel && logger.isErrorEnabled()) {
            return true;
        }
        if (LogLevel.DEBUG.code() == logLevel && logger.isDebugEnabled()) {
            return true;
        }
        if (LogLevel.TRACE.code() == logLevel && logger.isTraceEnabled()) {
            return true;
        }
        return false;
    }
}
