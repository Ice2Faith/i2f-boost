package i2f.core.zplugin.log.logger;

import i2f.core.zplugin.log.ILogger;
import i2f.core.zplugin.log.data.LogData;
import i2f.core.zplugin.log.enums.LogLevel;

/**
 * @author ltb
 * @date 2022/3/30 8:30
 * @desc
 */
public abstract class AbstractProxyLogger implements ILogger {
    public abstract void write(LogData log);
    @Override
    public void fatal(Object... content) {
        LogData log=LogData.instance()
                .withLevel(LogLevel.FATAL)
                .withContent(content);
        write(log);
    }

    @Override
    public void error(Object... content) {
        LogData log=LogData.instance()
                .withLevel(LogLevel.ERROR)
                .withContent(content);
        write(log);
    }

    @Override
    public void warn(Object... content) {
        LogData log=LogData.instance()
                .withLevel(LogLevel.WARN)
                .withContent(content);
        write(log);
    }

    @Override
    public void info(Object... content) {
        LogData log=LogData.instance()
                .withLevel(LogLevel.INFO)
                .withContent(content);
        write(log);
    }

    @Override
    public void debug(Object... content) {
        LogData log=LogData.instance()
                .withLevel(LogLevel.DEBUG)
                .withContent(content);
        write(log);
    }

    @Override
    public void trace(Object... content) {
        LogData log=LogData.instance()
                .withLevel(LogLevel.TRACE)
                .withContent(content);
        write(log);
    }
}
