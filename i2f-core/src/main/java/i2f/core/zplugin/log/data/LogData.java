package i2f.core.zplugin.log.data;

import i2f.core.zplugin.log.annotations.Log;
import i2f.core.zplugin.log.enums.LogLevel;
import i2f.core.zplugin.log.enums.LogType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author ltb
 * @date 2022/3/28 16:51
 * @desc
 */
@Data
@NoArgsConstructor
public class LogData {
    private String system;
    private String module;
    private String label;

    private LogLevel level;
    private LogType type;
    private Date date;
    private Long useTime;

    private String clazzName;

    private Object[] content;

    private Method method;

    private String methodName;
    private Integer line;
    private String fileName;
    private Boolean nativeMethod;

    private Object[] args;

    private Object ret;
    private Throwable ex;
    public static LogData instance(){
        LogData ret=new LogData();
        ret.date=new Date(System.currentTimeMillis());
        return ret;
    }
    public LogData withTrace(StackTraceElement trace){
        this.clazzName=trace.getClassName();
        this.methodName=trace.getMethodName();
        this.line=trace.getLineNumber();
        this.fileName=trace.getFileName();
        this.nativeMethod=trace.isNativeMethod();
        return this;
    }
    public LogData withMethod(Method method){
        this.method=method;
        this.methodName=method.getName();
        this.clazzName=method.getDeclaringClass().getName();
        return this;
    }
    public LogData withExcept(Throwable ex){
        this.ex=ex;
        return this;
    }
    public LogData withLevel(LogLevel level){
        this.level=level;
        return this;
    }
    public LogData withType(LogType type){
        this.type=type;
        return this;
    }
    public LogData withSystematics(String system,String module,String label){
        this.system=system;
        this.module=module;
        this.label=label;
        return this;
    }
    public LogData withLog(Log log){
        this.system=log.system();
        this.module=log.module();
        this.label=log.label();
        this.level=log.level();
        return this;
    }
    public LogData withClass(Class clazz){
        this.clazzName=clazz.getName();
        return this;
    }
    public LogData withClass(String className){
        this.clazzName=className;
        return this;
    }
    public LogData withArgs(Object ... args){
        this.args=args;
        return this;
    }
    public LogData withContent(Object ... content){
        this.content=content;
        return this;
    }
    public LogData withReturn(Object obj){
        this.ret=obj;
        return this;
    }
    public LogData withUseTime(long useTime){
        this.useTime=useTime;
        return this;
    }


    public static LogData before(Log log,Method method,Object ... args){
        return LogData.instance()
                .withLog(log)
                .withType(LogType.BEFORE)
                .withMethod(method)
                .withArgs(args);
    }
    public static LogData after(Log log,Method method,Object retVal,Object ... args){
        return LogData.instance()
                .withLog(log)
                .withType(LogType.AFTER)
                .withMethod(method)
                .withReturn(retVal)
                .withArgs(args);
    }
    public static LogData except(Log log,Method method,Throwable ex,Object ... args){
        return LogData.instance()
                .withLog(log)
                .withType(LogType.AFTER)
                .withMethod(method)
                .withExcept(ex)
                .withArgs(args);
    }

}
