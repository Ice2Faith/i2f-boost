package i2f.sql.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/4/8 16:51
 * @desc
 */
public class LambdaInflater {

    public static Set<Field> getFields(Class<?> clazz, Predicate<Field> filter, boolean matchedOne) {
        Set<Field> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (filter == null || filter.test(field)) {
                    ret.add(field);
                    if (matchedOne) {
                        return ret;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                if (filter == null || filter.test(field)) {
                    ret.add(field);
                    if (matchedOne) {
                        return ret;
                    }
                }
            }
        } catch (Exception e) {

        }

        if (matchedOne && !ret.isEmpty()) {
            return ret;
        }

        if (Object.class.equals(clazz)) {
            return ret;
        }
        Class<?> superClazz = clazz.getSuperclass();
        Set<Field> next = getFields(superClazz, filter, matchedOne);
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
        return path.replaceAll("\\\\|\\/", ".");
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
    public static Class<?> findClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Throwable e) {

        }
        try {
            Class<?> clazz = contextClassLoader().loadClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Throwable e) {

        }
        return null;
    }

    public static String fieldNameByMethodName(String methodName) {
        String[] prefixes = {"get", "set", "is", "with", "build"};
        for (String prefix : prefixes) {
            if (methodName.startsWith(prefix)) {
                methodName = methodName.substring(prefix.length());
            }
        }
        if ("".equals(methodName)) {
            return methodName;
        }
        return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
    }

    private static ConcurrentHashMap<String, Optional<Field>> fastFieldMap = new ConcurrentHashMap<>();

    public static Field fastSerializedLambdaFieldNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if (lambda == null) {
            return null;
        }
        String cacheKey = lambda.getImplClass() + "#" + lambda.getImplMethodName();
        Optional<Field> optional = fastFieldMap.get(cacheKey);
        if (optional != null) {
            return optional.orElse(null);
        }
        Field field = parseSerializedLambdaFieldNullable(lambda);
        fastFieldMap.put(cacheKey, Optional.ofNullable(field));
        return field;
    }

    public static Field getSerializedLambdaFieldNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        return parseSerializedLambdaFieldNullable(lambda);
    }

    public static Field parseSerializedLambdaFieldNullable(SerializedLambda lambda) {
        if (lambda == null) {
            return null;
        }
        String clazzName = path2ClassName(lambda.getImplClass());
        String methodName = lambda.getImplMethodName();
        Class<?> clazz = findClass(clazzName);
        if (clazz == null) {
            return null;
        }
        String fieldName = fieldNameByMethodName(methodName);
        Set<Field> set = getFields(clazz, (field) -> {
            if (field.getName().equals(fieldName)) {
                return true;
            }
            return false;
        }, true);
        if (!set.isEmpty()) {
            return set.iterator().next();
        }
        return null;
    }

    public static Class<?> getSerializedLambdaClassNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if (lambda == null) {
            return null;
        }
        String clazzName = path2ClassName(lambda.getImplClass());
        return findClass(clazzName);
    }


    public static SerializedLambda getSerializedLambdaNullable(Object obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        return LambdaInflater.getSerializedLambdaNullable((Serializable) obj);
    }

    public static SerializedLambda getSerializedLambdaNullable(Serializable fn) {
        try {
            return getSerializedLambda(fn);
        } catch (Exception e) {

        }
        return null;
    }

    public static SerializedLambda getSerializedLambdaNoExcept(Serializable fn) {
        try {
            return getSerializedLambda(fn);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }

    /**
     * 关键在于这个方法
     */
    public static SerializedLambda getSerializedLambda(Serializable fn) throws Exception {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
        return lambda;
    }
}
