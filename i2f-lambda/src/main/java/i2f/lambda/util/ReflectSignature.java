package i2f.lambda.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 17:07
 * @desc
 */

public class ReflectSignature {
    private static final Map<String, Class> simpleSignaturesNameTypeMap=new ConcurrentHashMap<>();
    private static final Map<Class, String> simpleSignaturesTypeNameMap = new ConcurrentHashMap<>();


    static{
        Map<String,Class> map=new HashMap<>();
        map.put("Z", boolean.class);
        map.put("B", byte.class);
        map.put("C", char.class);
        map.put("S", short.class);
        map.put("I", int.class);
        map.put("J", long.class);
        map.put("F", float.class);
        map.put("D", double.class);
        map.put("V", void.class);

        simpleSignaturesNameTypeMap.putAll(map);

        for (Map.Entry<String, Class> entry : map.entrySet()) {
            simpleSignaturesTypeNameMap.put(entry.getValue(),entry.getKey());
        }

    }

    public static Set<Method> getMethods(Class<?> clazz,Predicate<Method> filter,boolean matchedOne){
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


    public static Set<Field> getFields(Class<?> clazz,Predicate<Field> filter,boolean matchedOne){
        Set<Field> ret=new LinkedHashSet<>();
        if(clazz==null){
            return ret;
        }

        try{
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if(filter==null || filter.test(field)){
                    ret.add(field);
                    if(matchedOne){
                        return ret;
                    }
                }
            }
        }catch(Exception e){

        }

        try{
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                if(filter==null || filter.test(field)){
                    ret.add(field);
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
        Set<Field> next = getFields(superClazz, filter,matchedOne);
        ret.addAll(next);
        return ret;
    }

    /**
     * 路径转类名或包名
     *
     * @param path
     * @return
     */
    public static String path2ClassName(String path) {
        return path.replaceAll("\\\\|\\/",".");
    }

    /**
     * 包名或类名转路径
     *
     * @param className
     * @return
     */
    public static String className2Path(String className) {
        return className.replaceAll("\\.",File.separator);
    }

    /**
     * 包名或类名转URL的路径，也就是/分隔
     *
     * @param className
     * @return
     */
    public static String className2UrlPath(String className) {
        return className.replaceAll("\\.","/");
    }

    /**
     * 获取上下文类加载器
     *
     * @return
     */
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
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
            Class<?> clazz = contextClassLoader().loadClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static String fieldNameByMethodName(String methodName) {
        String[] prefixes={"get","set","is","with","build"};
        for (String prefix : prefixes) {
            if(methodName.startsWith(prefix)){
                methodName=methodName.substring(prefix.length());
            }
        }
        if("".equals(methodName)){
            return methodName;
        }
        return methodName.substring(0,1).toLowerCase()+methodName.substring(1);
    }

    public static <T> String toSign(Class<T> clazz) {
        String sign = simpleSignaturesTypeNameMap.get(clazz);
        if (sign == null) {
            if (clazz.isArray()) {
                sign = clazz.getName();
            } else {
                String name = clazz.getName();
                String path = className2UrlPath(name);
                sign = "L" + path + ";";
            }
        }
        return sign;
    }

    public static Class ofSign(String sign) {
        Class clazz = simpleSignaturesNameTypeMap.get(sign);
        if (clazz == null) {
            if (sign.startsWith("[")) {
                clazz = findClass(sign);
            } else {
                if (sign.startsWith("L")) {
                    sign = sign.substring(1);
                }
                if (sign.endsWith(";")) {
                    sign = sign.substring(0, sign.length() - 1);
                }
                String name = path2ClassName(sign);
                clazz = findClass(name);
            }
        }
        if (clazz == null) {
            clazz = findClass(sign);
        }
        return clazz;
    }

    public static String sign(Method method) {
        Parameter[] params = method.getParameters();
        Class<?> retType = method.getReturnType();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Parameter param : params) {
            builder.append(toSign(param.getType()));
        }
        builder.append(")");
        builder.append(toSign(retType));
        return builder.toString();
    }

    public static String sign(Field field) {
        return toSign(field.getType());
    }

}
