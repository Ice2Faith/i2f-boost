package i2f.trace;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 18:50
 * @desc
 */
public class ThreadTrace {
    public static final Class<?> CLAZZ=ThreadTrace.class;
    public static final String CLAZZ_NAME=CLAZZ.getName();

    public static StackTraceElement[] currentTrace(){
        StackTraceElement[] arr = Thread.currentThread().getStackTrace();
        int idx=0;
        for (int i = arr.length-1; i >=0; i--) {
            if(CLAZZ_NAME.equals(arr[i].getClassName())){
                idx=i+1;
                break;
            }
        }
        StackTraceElement[] ret=new StackTraceElement[arr.length-idx];
        for (int i = idx; i < arr.length; i++) {
            ret[i-idx]=arr[idx];
        }
        return ret;
    }

    public static StackTraceElement current(){
        return currentTrace()[0];
    }

    public static String currentClassName(){
        return current().getClassName();
    }

    public static String currentMethodName(){
        return current().getMethodName();
    }

    public static String currentFileName(){
        return current().getFileName();
    }

    public static int currentLineNumber(){
        return current().getLineNumber();
    }

    public static Class<?> currentClass(){
        String className = currentClassName();
        return findClass(className);
    }

    public static Method currentSingletonMethod(){
        StackTraceElement stack = current();
        String className = stack.getClassName();
        Class clazz = findClass(className);
        if(clazz==null){
            return null;
        }
        String methodName = stack.getMethodName();
        Set<Method> set = getMethods(clazz, (method) -> {
            if(!method.getName().equals(methodName)){
                return false;
            }
            return true;
        }, true);
        if(!set.isEmpty()){
            return set.iterator().next();
        }
        return null;
    }

    public static Method currentMethod(Object ... args){
        StackTraceElement stack = current();
        String className = stack.getClassName();
        Class clazz = findClass(className);
        if(clazz==null){
            return null;
        }
        String methodName = stack.getMethodName();
        Set<Method> set = getMethods(clazz, (method) -> {
            if(!method.getName().equals(methodName)){
                return false;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(!argsMatchTypes(parameterTypes,args)){
                return false;
            }
            return true;
        }, true);
        if(!set.isEmpty()){
            return set.iterator().next();
        }
        return null;
    }


    /**
     * 根据类名查找类
     * 找不到返回null
     *
     * @param className
     * @return
     */
    public static Class findClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {

        }
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static Set<Method> getMethods(Class<?> clazz, Predicate<Method> filter, boolean matchedOne){
        Set<Method> ret=new LinkedHashSet<>();
        if(clazz==null){
            return ret;
        }

        try{
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if(filter==null || filter.test(method)){
                    ret.add(method);
                    if(matchedOne){
                        return ret;
                    }
                }
            }
        }catch(Exception e){

        }

        try{
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(filter==null || filter.test(method)){
                    ret.add(method);
                    if(matchedOne){
                        return ret;
                    }
                }
            }
        }catch(Exception e){

        }

        if(matchedOne && !ret.isEmpty()){
            return ret;
        }

        if(Object.class.equals(clazz)){
            return ret;
        }
        Class<?> superClazz = clazz.getSuperclass();
        Set<Method> next = getMethods(superClazz, filter,matchedOne);
        ret.addAll(next);
        return ret;
    }

    public static final Class<?>[][] BASIC_TYPE_MAPPING={
            {int.class,Integer.class},
            {long.class,Long.class},
            {short.class,Short.class},
            {byte.class,Byte.class},
            {char.class,Character.class},
            {boolean.class,Boolean.class},
            {float.class,Float.class},
            {double.class,Double.class}
    };

    public static boolean isSameType(Class<?> clazz,Class<?> type){
        if(clazz.equals(type)){
            return true;
        }
        if(type.isAssignableFrom(clazz)){
            return true;
        }
        for (Class<?>[] arr : BASIC_TYPE_MAPPING) {
            if(arr[0].equals(clazz) && arr[1].equals(type)){
                return true;
            }
            if(arr[1].equals(clazz) && arr[0].equals(type)){
                return true;
            }
            if(arr[0].isAssignableFrom(clazz) && arr[1].isAssignableFrom(type)){
                return true;
            }
            if(arr[1].isAssignableFrom(clazz) && arr[0].isAssignableFrom(type)){
                return true;
            }
        }
        return false;
    }

    public static boolean argsMatchTypes(Class<?>[] types,Object ... args){
        if(types.length==0 && args.length==0){
            return true;
        }
        if(types.length!= args.length){
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if(args[i]==null){
                continue;
            }
            Class<?> type = types[i];
            if(type==null){
                continue;
            }
            Class<?> argType=args[i].getClass();
            if(!isSameType(argType,type)){
                return false;
            }
        }
        return true;
    }
}
