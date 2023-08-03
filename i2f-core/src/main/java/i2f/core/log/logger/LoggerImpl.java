package i2f.core.log.logger;

import i2f.core.log.consts.LogLevel;
import i2f.core.log.consts.LogSource;
import i2f.core.log.core.LogCore;
import i2f.core.log.core.LogDispatcher;
import i2f.core.log.data.LogDto;

/**
 * @author Ice2Faith
 * @date 2023/8/2 16:11
 * @desc
 */
public class LoggerImpl implements Logger {
    private String location;
    private String module;

    public LoggerImpl(String location) {
        this.location = location;
    }

    public LoggerImpl(String location, String module) {
        this.location = location;
        this.module = module;
    }

    public void write(LogLevel level,String label, Throwable ex, String format, Object ... params){
        LogDto dto = LogCore.newLog();
        dto.setLevel(level);
        dto.setLocation(location);
        dto.setModule(module);
        dto.setLabel(label);
        dto.setSource(LogSource.OUTPUT);
        Thread thread=Thread.currentThread();
        dto.getOrigin().setThread(thread);
        dto.setThread(thread.getName()+"-"+thread.getId());
        if(ex!=null){
            dto.getOrigin().setExcept(ex);
            dto.setExceptClass(ex.getClass().getName());
            dto.setExceptMsg(ex.getMessage());
            dto.setExceptStack(LogCore.stringifyStackTrace(ex));
        }
        dto.setContent(String.format(format,params));
        LogDispatcher.write(dto);
    }

    @Override
    public void info(String label, String format, Object... params) {
        write(LogLevel.INFO,label,null,format,params);
    }

    @Override
    public void info(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.INFO,label,ex,format,params);
    }

    @Override
    public void warn(String label, String format, Object... params) {
        write(LogLevel.WARN,label,null,format,params);
    }

    @Override
    public void warn(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.WARN,label,ex,format,params);
    }

    @Override
    public void error(String label, String format, Object... params) {
        write(LogLevel.ERROR,label,null,format,params);
    }

    @Override
    public void error(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.ERROR,label,ex,format,params);
    }

    @Override
    public void fatal(String label, String format, Object... params) {
        write(LogLevel.FATAL,label,null,format,params);
    }

    @Override
    public void fatal(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.FATAL,label,ex,format,params);
    }

    @Override
    public void debug(String label, String format, Object... params) {
        write(LogLevel.DEBUG,label,null,format,params);
    }

    @Override
    public void debug(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.DEBUG,label,ex,format,params);
    }

    @Override
    public void trace(String label, String format, Object... params) {
        write(LogLevel.TRACE,label,null,format,params);
    }

    @Override
    public void trace(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.TRACE,label,ex,format,params);
    }
}
