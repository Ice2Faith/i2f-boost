package i2f.core.trace;


import i2f.core.annotations.remark.Author;
import i2f.core.check.CheckUtil;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.thread.ThreadUtil;
import i2f.core.type.str.Appender;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Author("i2f")
public class TraceUtil {
    public Date emitDate = new Date();
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    public StackTraceElement lastTraceElem;
    public StackTraceElement parentTraceElem;
    public Class lastClass;
    public Method lastMethod;
    public Object lastObj;
    public Object[] methodArgs;
    public Object methodRetVal;

    public String msg;

    public TraceUtil() {
        getBaseInfos();
        prepare();
    }

    public TraceUtil(Object obj, Object retVal, Object... args) {
        getBaseInfos();
        getExtendInfos(obj, retVal, args);
        prepare();
    }

    public TraceUtil(String msg) {
        this.msg=msg;
        getBaseInfos();
        prepare();
    }

    public TraceUtil(String msg, Object obj, Object retVal, Object... args) {
        this.msg=msg;
        getBaseInfos();
        getExtendInfos(obj, retVal, args);
        prepare();
    }

    public String getClassSimpleName(){
        return lastClass.getSimpleName();
    }

    public String getMehodName(){
        return lastMethod.getName();
    }

    public String getClassFullName(){
        return lastClass.getName();
    }

    public String getFullMethodName(){
        return Appender.str(getClassFullName(),".",getMehodName());
    }

    private void prepare() {
        if (CheckUtil.isNull(lastClass)) {
            lastClass = lastObj.getClass();
        }
        if (CheckUtil.isNull(lastMethod)) {
            try {
                if (CheckUtil.notEmptyArray(methodArgs)) {
                    Class<?>[] types = new Class<?>[methodArgs.length];
                    for (int i = 0; i < methodArgs.length; i++) {
                        types[i] = methodArgs[i].getClass();
                    }
                    lastMethod = ReflectResolver.matchMethod(lastClass, lastTraceElem.getMethodName(), types);
                }
            } catch (Exception e) {

            }
            if (CheckUtil.isNull(lastMethod)) {
                String callMtdName = lastTraceElem.getMethodName();
                Method[] methods = lastClass.getDeclaredMethods();
                ArrayList<Method> sameMethods = new ArrayList<>();
                for (Method item : methods) {
                    if (item.getName().equals(callMtdName)) {
                        sameMethods.add(item);
                    }
                }
                if (sameMethods.size() > 0) {
                    lastMethod = sameMethods.get(0);
                }
            }
        }
    }

    public static StackTraceElement[] getStackTrace(){
        return ThreadUtil.currentThread().getStackTrace();
    }

    public static StackTraceElement getTrace(int offset){
        StackTraceElement[] traces=ThreadUtil.currentThread().getStackTrace();
        return traces[offset];
    }
    public static StackTraceElement[] getTraces(int offset){
        StackTraceElement[] traces=ThreadUtil.currentThread().getStackTrace();
        StackTraceElement[] ret=new StackTraceElement[traces.length-offset];
        for(int i=0;i<ret.length;i++){
            ret[i]=traces[i+offset];
        }
        return ret;
    }

    private void getBaseInfos() {
        StackTraceElement[] traces = getStackTrace();
        lastTraceElem = traces[3];
        if (traces.length >= 5) {
            parentTraceElem = traces[4];
        }
        try {
            lastClass = Class.forName(lastTraceElem.getClassName());
        } catch (Exception e) {
            //
        }
    }

    private void getExtendInfos(Object obj, Object retVal, Object... args) {
        lastObj = obj;
        methodRetVal = retVal;
        methodArgs = args;
        if (CheckUtil.notEmptyArray(args)) {
            Class[] methodParamsType = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                methodParamsType[i] = args[i].getClass();
            }
            try {
                lastMethod = ReflectResolver.matchMethod(lastClass, lastTraceElem.getMethodName(), methodParamsType);
            } catch (Exception e) {

            }
        }

    }

    @Override
    public String toString() {
        String ret = Appender.buffer().adds(
        "Tracer -> Class:",lastTraceElem.getClassName()
                , "\n\tMethod:" , lastTraceElem.getMethodName()
                ,(msg==null?"":"\n\tTag:"+msg)
                , "\n\tLine:" , lastTraceElem.getLineNumber()
                , "\n\tFile:" , lastTraceElem.getFileName()
                , "\n\tTime:" , sdf.format(emitDate)
                , "\n\tLocation:" , lastTraceElem
                , "\n\tParent:" , (parentTraceElem == null ? "none" : parentTraceElem)
                , "\n").get();

        String param = "";
        String retStr = "";

        Appender<StringBuilder> prototype= Appender.builder();
        if (CheckUtil.isNull(lastMethod)) {
            prototype.adds("unknown " ,lastTraceElem.getMethodName() , "(unknown args)");
        } else {
            prototype.add(Modifier.toString(lastMethod.getModifiers())) ;
            prototype.adds(" " , lastMethod.getName(),"(");
            Parameter[] pers = lastMethod.getParameters();
            for (int i = 0; i < pers.length; i++) {
                if (i != 0) {
                    prototype.add(" , ");
                }
                Parameter item = pers[i];
                prototype.adds(item.getName(),":");
                if (CheckUtil.notEmptyArray(methodArgs)) {
                    prototype.add(methodArgs[i]);
                } else {
                    prototype.add("unknown");
                }
                prototype.adds("[" , item.getType().getSimpleName() , "]");
            }
            prototype.add(")");
        }


        retStr += Appender.builder().adds(methodRetVal
                ,"[" , (methodRetVal == null ? (lastMethod == null ? "null/void" : lastMethod.getReturnType().getSimpleName()) : methodRetVal.getClass().getSimpleName()) , "]"
                ,"\tDetail:"
                ,"\n\t\tPrototype:" , prototype.get()
                ,"\n\t\tReturnVal:" , retStr
                ,"\n").get();

        return ret;
    }

    public void printLastTrace() {
        System.out.println(this.toString());
    }
    public static StackTraceElement[] getHereTraces() {
        StackTraceElement[] traces = getStackTrace();
        StackTraceElement[] ret=new StackTraceElement[traces.length-2];
        for (int i = 0; i < ret.length; i++) {
            ret[i]=traces[i+2];
        }
        return ret;
    }
    public static StackTraceElement[] getHereInvokerTraces() {
        StackTraceElement[] traces = getStackTrace();
        if(traces.length>=4) {
            StackTraceElement[] ret=new StackTraceElement[traces.length-3];
            for (int i = 0; i < ret.length; i++) {
                ret[i]=traces[i+3];
            }
            return ret;
        }
        return new StackTraceElement[0];
    }

    public static StackTraceElement getHereTrace() {
        StackTraceElement[] traces = getStackTrace();
        return traces[2];
    }
    public static StackTraceElement getHereInvokerTrace() {
        StackTraceElement[] traces = getStackTrace();
        if(traces.length>=4) {
            return traces[3];
        }
        return null;
    }
    public static String getHereClass() {
        StackTraceElement[] traces = getStackTrace();
        return traces[2].getClassName();
    }
    public static String getHereMethod() {
        StackTraceElement[] traces = getStackTrace();
        return traces[2].getMethodName();
    }
    public static String getHereFileName() {
        StackTraceElement[] traces = getStackTrace();
        return traces[2].getFileName();
    }
    public static int getHereLineNumber() {
        StackTraceElement[] traces = getStackTrace();
        return traces[2].getLineNumber();
    }

    public static String getHereInvokerClass() {
        StackTraceElement[] traces = getStackTrace();
        if(traces.length>=4) {
            return traces[3].getClassName();
        }
        return "jvm";
    }
    public static String getHereInvokerMethod() {
        StackTraceElement[] traces = getStackTrace();
        if(traces.length>=4) {
            return traces[3].getMethodName();
        }
        return "jvm";
    }
    public static String getHereInvokerFileName() {
        StackTraceElement[] traces = getStackTrace();
        if(traces.length>=4) {
            return traces[3].getFileName();
        }
        return "jvm";
    }
    public static int getHereInvokerLineNumber() {
        StackTraceElement[] traces = getStackTrace();
        if(traces.length>=4) {
            return traces[3].getLineNumber();
        }
        return -1;
    }
}
