package i2f.core.zplugin.log;

/**
 * @author ltb
 * @date 2022/3/30 8:27
 * @desc
 */
public interface ILogger {
    void fatal(Object ... content);
    void error(Object ... content);
    void warn(Object ... content);
    void info(Object ... content);
    void debug(Object ... content);
    void trace(Object ... content);
}
