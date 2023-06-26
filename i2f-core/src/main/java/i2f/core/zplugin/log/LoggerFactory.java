package i2f.core.zplugin.log;

import i2f.core.trace.TraceUtil;
import i2f.core.zplugin.log.context.LogWriterHolder;
import i2f.core.zplugin.log.controls.LogDecision;
import i2f.core.zplugin.log.enums.LogType;
import i2f.core.zplugin.log.impl.DecisionLogWriterAdapter;
import i2f.core.zplugin.log.logger.SimpleLogger;

/**
 * @author ltb
 * @date 2022/3/30 9:19
 * @desc
 */
public class LoggerFactory {
    public static ILogger getLogger(String className, ILogWriter writer){
        return new SimpleLogger(getDecisionLogWriter(writer))
                .withType(LogType.DIRECT)
                .withClass(className);
    }
    public static ILogger getLogger(Class clazz, ILogWriter writer){
        return new SimpleLogger(getDecisionLogWriter(writer))
                .withType(LogType.DIRECT)
                .withClass(clazz);
    }
    public static ILogger getLogger(ILogWriter writer){
        StackTraceElement elem = TraceUtil.getHereTrace();
        return new SimpleLogger(getDecisionLogWriter(writer))
                .withType(LogType.DIRECT)
                .withClass(elem.getClassName());
    }
    public static ILogger getLogger(String system,String module,String label,String className,ILogWriter writer){
        return new SimpleLogger(getDecisionLogWriter(writer))
                .withType(LogType.DIRECT)
                .withSystematics(system, module, label)
                .withClass(className);
    }
    public static ILogger getLogger(String system,String module,String label,Class clazz,ILogWriter writer){
        return new SimpleLogger(getDecisionLogWriter(writer))
                .withType(LogType.DIRECT)
                .withSystematics(system, module, label)
                .withClass(clazz);
    }
    public static ILogger getLogger(String system,String module,String label,ILogWriter writer){
        StackTraceElement elem = TraceUtil.getHereTrace();
        return new SimpleLogger(getDecisionLogWriter(writer))
                .withType(LogType.DIRECT)
                .withSystematics(system, module, label)
                .withClass(elem.getClassName());
    }

    public static ILogger getLogger(String className){
        return new SimpleLogger(LogWriterHolder.getWriter())
                .withType(LogType.DIRECT)
                .withClass(className);
    }
    public static ILogger getLogger(Class clazz){
        return new SimpleLogger(LogWriterHolder.getWriter())
                .withType(LogType.DIRECT)
                .withClass(clazz);
    }
    public static ILogger getLogger(){
        StackTraceElement elem = TraceUtil.getHereTrace();
        return new SimpleLogger(LogWriterHolder.getWriter())
                .withType(LogType.DIRECT)
                .withClass(elem.getClassName());
    }
    public static ILogger getLogger(String system,String module,String label,String className){
        return new SimpleLogger(LogWriterHolder.getWriter())
                .withType(LogType.DIRECT)
                .withSystematics(system, module, label)
                .withClass(className);
    }
    public static ILogger getLogger(String system,String module,String label,Class clazz){
        return new SimpleLogger(LogWriterHolder.getWriter())
                .withType(LogType.DIRECT)
                .withSystematics(system, module, label)
                .withClass(clazz);
    }
    public static ILogger getLogger(String system,String module,String label){
        StackTraceElement elem = TraceUtil.getHereTrace();
        return new SimpleLogger(LogWriterHolder.getWriter())
                .withType(LogType.DIRECT)
                .withSystematics(system, module, label)
                .withClass(elem.getClassName());
    }

    public static LogProxyHandler getHandler(ILogWriter writer){
        return new LogProxyHandler(getDecisionLogWriter(writer));
    }

    public static LogProxyHandler getHandler(){
        return new LogProxyHandler(LogWriterHolder.getWriter());
    }

    public static ILogWriter getDecisionLogWriter(ILogWriter writer){
        if(writer instanceof DecisionLogWriterAdapter){
            return writer;
        }
        return new DecisionLogWriterAdapter(writer);
    }

    public static ILogWriter getDecisionLogWriter(ILogWriter writer, LogDecision decision){
        if(writer instanceof DecisionLogWriterAdapter){
            return writer;
        }
        return new DecisionLogWriterAdapter(writer,decision);
    }
}
