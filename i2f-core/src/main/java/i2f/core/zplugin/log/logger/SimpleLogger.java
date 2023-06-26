package i2f.core.zplugin.log.logger;

import i2f.core.trace.TraceUtil;
import i2f.core.zplugin.log.ILogWriter;
import i2f.core.zplugin.log.data.LogData;
import i2f.core.zplugin.log.enums.LogType;

/**
 * @author ltb
 * @date 2022/3/30 9:09
 * @desc
 */
public class SimpleLogger extends AbstractProxyLogger {
    private ILogWriter writer;
    public String system;
    public String module;
    public String label;
    public LogType type=LogType.DIRECT;
    public String className;
    public SimpleLogger(ILogWriter writer){
        this.writer=writer;
    }
    public SimpleLogger withSystematics(String system,String module,String label){
        this.system=system;
        this.module=module;
        this.label=label;
        return this;
    }
    public SimpleLogger withClass(Class clazz){
        this.className=clazz.getName();
        return this;
    }
    public SimpleLogger withClass(String className){
        this.className=className;
        return this;
    }
    public SimpleLogger withType(LogType type){
        this.type=type;
        return this;
    }

    @Override
    public void write(LogData log) {
        log.withSystematics(system,module,label);
        if(log.getClazzName()==null){
            log.withClass(className);
        }
        if(log.getType()==null){
            log.withType(type);
        }
        if(log.getLine()==null){
            StackTraceElement elem = TraceUtil.getHereTrace();
            log.withTrace(elem);
        }
        writer.write(log);
    }

}
