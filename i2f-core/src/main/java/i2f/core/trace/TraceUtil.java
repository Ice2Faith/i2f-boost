package i2f.core.trace;


import i2f.core.annotations.remark.Author;
import i2f.core.check.CheckUtil;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.type.str.Appender;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

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


    private void getBaseInfos() {
        StackTraceElement[] traces = getTrace();
        lastTraceElem = traces[0];
        if (traces.length >= 2) {
            parentTraceElem = traces[1];
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
        return getTrace();
    }

    public static StackTraceElement getHereTrace() {
        StackTraceElement[] traces = getTrace();
        if (traces.length == 0) {
            return null;
        }
        return traces[0];
    }
    public static StackTraceElement getHereInvokerTrace() {
        StackTraceElement[] traces = getTrace();
        if (traces.length >= 1) {
            return traces[1];
        }
        return null;
    }
    public static String getHereClass() {
        StackTraceElement trace = getHereTrace();
        if (trace != null) {
            return trace.getClassName();
        }
        return "jvm";
    }
    public static String getHereMethod() {
        StackTraceElement trace = getHereTrace();
        if (trace != null) {
            return trace.getMethodName();
        }
        return "jvm";
    }
    public static String getHereFileName() {
        StackTraceElement trace = getHereTrace();
        if (trace != null) {
            return trace.getFileName();
        }
        return "jvm";
    }
    public static int getHereLineNumber() {
        StackTraceElement trace = getHereTrace();
        if (trace != null) {
            return trace.getLineNumber();
        }
        return -1;
    }

    public static String getHereInvokerClass() {
        StackTraceElement trace = getHereInvokerTrace();
        if (trace != null) {
            return trace.getClassName();
        }
        return "jvm";
    }
    public static String getHereInvokerMethod() {
        StackTraceElement trace = getHereInvokerTrace();
        if (trace != null) {
            return trace.getMethodName();
        }
        return "jvm";
    }
    public static String getHereInvokerFileName() {
        StackTraceElement trace = getHereInvokerTrace();
        if (trace != null) {
            return trace.getFileName();
        }
        return "jvm";
    }

    public static int getHereInvokerLineNumber() {
        StackTraceElement trace = getHereInvokerTrace();
        if (trace != null) {
            return trace.getLineNumber();
        }
        return -1;
    }


    public static final Class<?> TRACE_CLASS = TraceUtil.class;
    public static final String TRACE_CLASS_NAME = TRACE_CLASS.getName();

    public static StackTraceElement[] getTrace() {
        return getTrace(null);
    }

    public static StackTraceElement[] getTraceWithoutJava() {
        return getTrace((elem) -> {
            String className = elem.getClassName();
            if (className.startsWith("java.")) {
                return false;
            }
            if (className.startsWith("javax.")) {
                return false;
            }
            if (className.startsWith("javafx.")) {
                return false;
            }
            if (className.startsWith("sun.")) {
                return false;
            }
            return true;
        });
    }

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
}
