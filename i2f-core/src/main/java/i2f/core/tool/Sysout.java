package i2f.core.tool;

import i2f.core.str.Appender;
import i2f.core.trace.TraceUtil;

import java.io.PrintStream;

/**
 * @author ltb
 * @date 2021/8/12
 */
public class Sysout {
    public static PrintStream out=System.out;
    public static String DEFAULT_SEPARATOR=",";
    public static void line(Object ... args){
        out.println(Appender.str(args));
    }
    public static void lineSep(Object ... args){
        out.println(Appender.sepStr(DEFAULT_SEPARATOR,args));
    }
    public static void lineSep(String sep,Object ... args){
        out.println(Appender.sepStr(sep,args));
    }
    public static void val(String valName, Object val){
        String type="";
        if(val!=null){
            Class clazz=val.getClass();
            type= clazz.getSimpleName();
        }
        out.println(Appender.str(valName,"(",type,")","=",val));
    }
    public static void log(Object ... args){
        StackTraceElement elem= TraceUtil.getHereInvokerTrace();
        out.println(Appender.builder().adds(elem.getClassName(),"->",elem.getMethodName()," line:"+elem.getLineNumber()," : ").adds(args).get());
    }
    public static void logSep(Object ... args){
        StackTraceElement elem= TraceUtil.getHereInvokerTrace();
        out.println(Appender.builder().adds(elem.getClassName(),"->",elem.getMethodName()," line:"+elem.getLineNumber()," : ").adds(DEFAULT_SEPARATOR,args).get());
    }
    public static void logSep(String sep,Object ... args){
        StackTraceElement elem= TraceUtil.getHereInvokerTrace();
        out.println(Appender.builder().adds(elem.getClassName(),"->",elem.getMethodName()," line:"+elem.getLineNumber()," : ").addsSep(sep,args).get());
    }
}
