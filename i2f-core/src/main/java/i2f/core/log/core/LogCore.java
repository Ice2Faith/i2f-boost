package i2f.core.log.core;

import i2f.core.check.CheckUtil;
import i2f.core.log.annotations.Log;
import i2f.core.log.consts.LogLevel;
import i2f.core.log.consts.LogSource;
import i2f.core.log.data.LogAnnDto;
import i2f.core.log.data.LogDto;
import i2f.core.log.data.LogOriginDto;
import i2f.core.serialize.str.IStringObjectSerializer;
import i2f.std.invoke.IProxyInvokable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/8/2 9:03
 * @desc
 */
public class LogCore  {

    public static LogAnnDto getLogAnn(Method method){
        Log mann = getMemberAnnotation(method, Log.class);
        Log cann = getAnnotation(method.getDeclaringClass(), Log.class);
        if(mann==null && cann==null){
            return null;
        }
        LogAnnDto ret=new LogAnnDto();
        if(cann!=null){
            ret.setValue(cann.value());
            ret.setBefore(cann.before());
            ret.setAfter(cann.after());
            ret.setThrowing(cann.throwing());
            ret.setThrowLevel(cann.throwLevel());
            ret.setModule(cann.module());
            ret.setLabel(cann.label());
        }
        if(mann!=null){
            ret.setValue(mann.value());
            ret.setBefore(mann.before());
            ret.setAfter(mann.after());
            ret.setThrowing(mann.throwing());
            ret.setThrowLevel(mann.throwLevel());
            if(!CheckUtil.isEmptyStr(mann.module())){
                ret.setModule(mann.module());
            }
            if(!CheckUtil.isEmptyStr(mann.label())){
                ret.setLabel(mann.label());
            }
        }
        return ret;
    }

    public static LogDto newLog(){
        LogDto dto=new LogDto();
        dto.setTime(new Date());
        dto.setLevel(LogLevel.INFO);
        dto.setLocation(null);

        dto.setSystem(LogContext.system);
        dto.setModule(null);
        dto.setLabel(null);

        dto.setSource(null);

        dto.setThread(null);

        dto.setContent(null);

        dto.setClazz(null);
        dto.setMethod(null);
        dto.setArgs(null);
        dto.setRet(null);
        dto.setUseMillSeconds(-1);

        dto.setExceptClass(null);
        dto.setExceptMsg(null);
        dto.setExceptStack(null);

        dto.setOrigin(new LogOriginDto());
        return dto;
    }

    public static String methodString(Method method){
        StringBuilder builder=new StringBuilder();
        int modifiers = method.getModifiers();
        if(Modifier.isPublic(modifiers)){
            builder.append("public");
        }
        if(Modifier.isPrivate(modifiers)){
            builder.append("private");
        }
        if(Modifier.isProtected(modifiers)){
            builder.append("protected");
        }
        if(Modifier.isStatic(modifiers)){
            builder.append(" ").append("static");
        }
        if(Modifier.isNative(modifiers)){
            builder.append(" ").append("native");
        }
        if(Modifier.isSynchronized(modifiers)){
            builder.append(" ").append("synchronized");
        }

        Class<?> returnType = method.getReturnType();
        builder.append(" ").append(returnType.getSimpleName());

        Class<?> declaringClass = method.getDeclaringClass();
        builder.append(" ").append(declaringClass.getSimpleName());

        builder.append(".").append(method.getName());

        builder.append("(");
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if(i!=0){
                builder.append(", ");
            }
            builder.append(parameters[i].getType().getSimpleName());
            builder.append(" ").append(parameters[i].getName());
        }
        builder.append(")");

        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if(exceptionTypes.length>0){
            builder.append(" throws ");
            for (int i = 0; i < exceptionTypes.length; i++) {
                if(i!=0){
                    builder.append(", ");
                }
                builder.append(exceptionTypes[i].getSimpleName());
            }
        }

        return builder.toString();
    }

    public static LogDto invokeBefore(Method method,Object... args){
        LogAnnDto ann = getLogAnn(method);
        if(ann!=null){
            if(!ann.isValue()){
                return null;
            }
            if(!ann.isBefore()){
                return null;
            }
        }else{
            if(!LogContext.defaultBefore.get()){
                return null;
            }
        }


        LogDto dto = newLog();
        fillInvokeMethod(method, ann, dto);
        dto.setContent("before invoke "+dto.getMethod());
        dto.getOrigin().setArgs(args);
        dto.setArgs(stringifyArgs(LogContext.serializer,args));
        dto.setRet(null);
        dto.setExceptClass(null);
        dto.setExceptMsg(null);
        dto.setExceptStack(null);
        return dto;
    }

    public static LogDto invokeAfter(Method method,Object retVal,long useMillSeconds,Object... args){
        LogAnnDto ann = getLogAnn(method);
        if(ann!=null){
            if(!ann.isValue()){
                return null;
            }
            if(!ann.isAfter()){
                return null;
            }
        }else{
            if(!LogContext.defaultAfter.get()){
                return null;
            }
        }

        LogDto dto = newLog();
        fillInvokeMethod(method, ann, dto);
        dto.setContent("after invoke "+dto.getMethod());
        dto.getOrigin().setArgs(args);
        dto.getOrigin().setRet(retVal);
        dto.setArgs(stringifyArgs(LogContext.serializer,args));
        dto.setRet(stringify(LogContext.serializer,retVal));
        dto.setUseMillSeconds(useMillSeconds);
        dto.setExceptClass(null);
        dto.setExceptMsg(null);
        dto.setExceptStack(null);

        return dto;
    }
    public static LogDto invokeThrowing(Method method,Throwable ex,long useMillSeconds,Object... args){
        LogAnnDto ann = getLogAnn(method);
        if(ann!=null){
            if(!ann.isValue()){
                return null;
            }
            if(!ann.isThrowing()){
                return null;
            }
        }else{
            if(!LogContext.defaultThrowing.get()){
                return null;
            }
        }

        LogDto dto = newLog();
        if(ann!=null){
            LogLevel level = ann.getThrowLevel();
            dto.setLevel(level);
        }
        dto.setContent("throwing error "+ex.getMessage());
        fillInvokeMethod(method, ann, dto);
        dto.getOrigin().setArgs(args);
        dto.getOrigin().setExcept(ex);
        dto.setArgs(stringifyArgs(LogContext.serializer,args));
        dto.setRet(null);
        dto.setUseMillSeconds(useMillSeconds);
        dto.setExceptClass(ex.getClass().getName());
        dto.setExceptMsg(ex.getMessage());
        dto.setExceptStack(stringifyStackTrace(ex));
        return dto;
    }


    public static Object aopAround(IProxyInvokable pjp) throws Throwable {
        Method method = pjp.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] params = method.getParameters();

        Object ret=null;
        Throwable ex=null;
        long beginTs=System.currentTimeMillis();
        try{
            ret = pjp.invoke();
            return ret;
        }catch(Throwable e){
            ex=e;
            throw e;
        }finally {
            if(method!=null){
                LogDto dto = invokeBefore(method, args);
                if(dto!=null){
                    LogDispatcher.write(dto);
                }
            }
            long useMillSeconds=System.currentTimeMillis()-beginTs;
            if(ex!=null){
                if(method!=null){
                    LogDto dto = invokeThrowing(method,ex,useMillSeconds, args);
                    if(dto!=null){
                        LogDispatcher.write(dto);
                    }
                }
            }else{
                if(method!=null){
                    LogDto dto = invokeAfter(method,ret,useMillSeconds, args);
                    if(dto!=null){
                        LogDispatcher.write(dto);
                    }
                }
            }
        }
    }


    public static<T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz){
        T ann = elem.getDeclaredAnnotation(clazz);
        if(ann==null){
            ann=elem.getAnnotation(clazz);
        }
        return ann;
    }

    public static<T extends Annotation> T getMemberAnnotation(Method member, Class<T> clazz){
        T ann = getAnnotation(member, clazz);
        if(ann==null){
            Class<?> declaringClass = member.getDeclaringClass();
            ann = getAnnotation(declaringClass, clazz);
        }
        return ann;
    }


    public static LogDto fillInvokeMethod(Method method, LogAnnDto ann, LogDto dto) {
        dto.setLocation(method.getDeclaringClass().getName());
        if(ann !=null){
            dto.setModule(ann.getModule());
        }
        if(ann !=null){
            dto.setLabel(ann.getLabel());
        }
        dto.setSource(LogSource.AOP);
        Thread thread = Thread.currentThread();

        dto.getOrigin().setAnn(ann);
        dto.getOrigin().setMethod(method);
        dto.getOrigin().setThread(thread);

        dto.setThread(thread.getName()+"-"+thread.getId());
        dto.setContent(null);
        dto.setClazz(method.getDeclaringClass().getName());
        dto.setMethod(methodString(method));
        return dto;
    }

    public static String stringifyArgs(IStringObjectSerializer serializer, Object... args){
        StringBuilder builder=new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            Object val = args[i];
            String str = stringify(serializer, val);
            builder.append("arg").append(i).append(" = ").append(str).append("\n");
        }
        return builder.toString();
    }
    public static String stringify(IStringObjectSerializer serializer, Object val){
        try{
            return serializer.serialize(val);
        }catch(Exception e){

        }
        return val.getClass().getName()+"@"+val.hashCode();
    }

    public static String stringifyStackTrace(Throwable ex){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        PrintStream ps=new PrintStream(bos);
        ex.printStackTrace(ps);
        ps.flush();
        ps.close();
        return new String(bos.toByteArray());
    }

}
