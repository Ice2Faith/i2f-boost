package i2f.core.log.logger;

import i2f.core.check.CheckUtil;
import i2f.core.log.annotations.Log;
import i2f.core.log.consts.LogLevel;
import i2f.core.log.consts.LogSource;
import i2f.core.log.core.LogCore;
import i2f.core.log.core.LogDispatcher;
import i2f.core.log.data.LogDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2023/8/3 14:13
 * @desc
 */
public class Lg {
    private static Class<?> TRACE_CLASS=Lg.class;
    private static String TRACE_CLASS_NAME=TRACE_CLASS.getName();

    public static StackTraceElement[] getTrace(Predicate<StackTraceElement> filter) {
        StackTraceElement[] elems = Thread.currentThread().getStackTrace();
        List<StackTraceElement> ret = new ArrayList<>(Math.max(32, elems.length));
        int idx = elems.length - 1;
        while (idx >= 0) {
            StackTraceElement item = elems[idx];
            String className = item.getClassName();
            if (TRACE_CLASS_NAME.equals(className)) {
                idx++;
                break;
            }
            idx--;
        }
        idx = Math.max(0, idx);
        while (idx < elems.length) {
            StackTraceElement item = elems[idx];
            idx++;
            if (filter != null) {
                if (!filter.test(item)) {
                    continue;
                }
            }
            ret.add(item);
        }

        return ret.toArray(new StackTraceElement[0]);
    }

    public static StackTraceElement getHereTrace() {
        StackTraceElement[] traces = getTrace(null);
        if (traces.length == 0) {
            return null;
        }
        return traces[0];
    }

    public static String getRealClassName(String className) {
        int idx = className.indexOf("$$Enhancer");
        if (idx >= 0) {
            className = className.substring(0, idx);
        }
        return className;
    }

    public static Class<?> loadClassByName(String className){
        Class<?> clazz = null;
        try{
            clazz=Class.forName(className);
            if(clazz!=null){
                return clazz;
            }
        }catch(Exception e){

        }
        try{
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            if(clazz!=null){
                return clazz;
            }
        }catch(Exception e){

        }
        return null;
    }

    public static ConcurrentHashMap<String, Optional<String>> classNameModuleMap=new ConcurrentHashMap<>();
    public static String getModuleByClass(String className){
        if(classNameModuleMap.containsKey(className)){
            return classNameModuleMap.get(className).orElse(null);
        }
        String ret=null;
        try{
            Class<?> clazz = loadClassByName(className);
            Log ann = LogCore.getAnnotation(clazz, Log.class);
            if(ann!=null){
                if(!CheckUtil.isEmptyStr(ann.module())){
                    ret= ann.module();
                }
            }
        }catch(Exception e){

        }
        classNameModuleMap.put(className,Optional.ofNullable(ret));
        return ret;
    }

    public static LogDto locationByTrace(LogDto dto){
        StackTraceElement trace = getHereTrace();
        if(trace==null){
            return dto;
        }
        String className = getRealClassName(trace.getClassName());
        dto.setLocation(className);
        String module = getModuleByClass(className);
        dto.setModule(module);

        return dto;
    }

    public static void write(LogLevel level, String label, Throwable ex, String format, Object ... params){
        LogDto dto = LogCore.newLog();
        dto.setLevel(level);
        locationByTrace(dto);
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


    public static void info(String label, String format, Object... params) {
        write(LogLevel.INFO,label,null,format,params);
    }


    public static void info(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.INFO,label,ex,format,params);
    }


    public static void warn(String label, String format, Object... params) {
        write(LogLevel.WARN,label,null,format,params);
    }


    public static void warn(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.WARN,label,ex,format,params);
    }


    public static void error(String label, String format, Object... params) {
        write(LogLevel.ERROR,label,null,format,params);
    }


    public static void error(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.ERROR,label,ex,format,params);
    }


    public static void fatal(String label, String format, Object... params) {
        write(LogLevel.FATAL,label,null,format,params);
    }


    public static void fatal(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.FATAL,label,ex,format,params);
    }


    public static void debug(String label, String format, Object... params) {
        write(LogLevel.DEBUG,label,null,format,params);
    }


    public static void debug(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.DEBUG,label,ex,format,params);
    }


    public static void trace(String label, String format, Object... params) {
        write(LogLevel.TRACE,label,null,format,params);
    }


    public static void trace(String label, Throwable ex, String format, Object... params) {
        write(LogLevel.TRACE,label,ex,format,params);
    }
}
