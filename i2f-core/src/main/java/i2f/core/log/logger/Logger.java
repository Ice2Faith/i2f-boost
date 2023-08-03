package i2f.core.log.logger;

/**
 * @author Ice2Faith
 * @date 2023/8/2 11:11
 * @desc
 */
public interface Logger {
    void info(String label,String format,Object ... params);
    void info(String label,Throwable ex,String format,Object... params);

    void warn(String label,String format,Object ... params);
    void warn(String label,Throwable ex,String format,Object... params);

    void error(String label,String format,Object ... params);
    void error(String label,Throwable ex,String format,Object... params);

    void fatal(String label,String format,Object ... params);
    void fatal(String label,Throwable ex,String format,Object... params);

    void debug(String label,String format,Object ... params);
    void debug(String label,Throwable ex,String format,Object... params);

    void trace(String label,String format,Object ... params);
    void trace(String label,Throwable ex,String format,Object... params);
}
