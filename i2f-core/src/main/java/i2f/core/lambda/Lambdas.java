package i2f.core.lambda;

import i2f.core.exception.BoostException;
import i2f.core.lambda.functional.IFunction;
import i2f.core.lambda.functional.consumer.*;
import i2f.core.lambda.functional.provider.*;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/11/9 12:49
 * @desc
 */
public class Lambdas {

    public static String path2className(String path) {
        return path.replaceAll("/", ".").replaceAll("\\\\", ".");
    }

    public static Method implMethod(SerializedLambda lambda) {
        try {
            String implClassName = path2className(lambda.getImplClass());
            Class implClass = Class.forName(implClassName);
            Method implMethod = findMethodBySignature(implClass,
                    lambda.getImplMethodName(),
                    lambda.getImplMethodSignature());
            return implMethod;
        } catch (Exception e) {
            throw new BoostException(e.getMessage(), e);
        }
    }

    public static Field implBindField(SerializedLambda lambda) {
        try {
            String implClassName = path2className(lambda.getImplClass());
            Class implClass = Class.forName(implClassName);
            String fieldName = fieldNameByMethodName(lambda.getImplMethodName());
            return findFieldByName(implClass, fieldName);
        } catch (Exception e) {
            throw new BoostException(e.getMessage(), e);
        }
    }

    public static String methodName(SerializedLambda lambda) {
        return lambda.getImplMethodName();
    }

    public static String fieldName(SerializedLambda lambda) {
        return fieldNameByMethodName(lambda.getImplMethodName());
    }

    /**
     * 字符串首字母转小写
     */
    public static String firstLower(String field) {
        return field.substring(0, 1).toLowerCase() + field.substring(1);
    }

    public static String trimPrefix(String name, String... prefixes) {
        for (String item : prefixes) {
            if (name.startsWith(item)) {
                name = name.substring(item.length());
                break;
            }
        }
        return name;
    }

    public static String fieldNameByMethodName(String methodName) {
        String fieldName = trimPrefix(methodName, "get", "set", "is", "with", "build");
        return firstLower(fieldName);
    }

    public static Field findFieldByName(Class<?> clazz, String name) {
        Set<Field> fields = new HashSet<>();
        for (Field field : clazz.getFields()) {
            if (field.getName().equals(name)) {
                fields.add(field);
            }
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                fields.add(field);
            }
        }
        int size = fields.size();
        if (size == 0) {
            return null;
        }
        return fields.iterator().next();
    }

    public static Method findMethodBySignature(Class<?> clazz, String name, String signature) {
        Set<Method> methods = new HashSet<>();
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(name)) {
                methods.add(method);
            }
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                methods.add(method);
            }
        }
        int size = methods.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return methods.iterator().next();
        }
        // 实际上，再实现lambda过程中，如果是方法引用的方式，将会出现指向不明确的情况，因此，次需要单纯判断参数个数即可
        String[] arr = signature.split("\\)");
        String argsLine = arr[0].substring(1);
        if (argsLine.endsWith(";")) {
            argsLine = argsLine.substring(0, argsLine.length() - 1);
        }
        String[] args = argsLine.split(";");
        Iterator<Method> iterator = methods.iterator();
        while (iterator.hasNext()) {
            Method next = iterator.next();
            if (next.getParameterCount() == args.length) {
                return next;
            }
        }
        return methods.iterator().next();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Field implBindField(IConsumer0 fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1> Field implBindField(IConsumer1<V1> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2> Field implBindField(IConsumer2<V1, V2> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Field implBindField(IConsumer3<V1, V2, V3> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> Field implBindField(IConsumer4<V1, V2, V3, V4> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> Field implBindField(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> Field implBindField(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> Field implBindField(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> Field implBindField(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Field implBindField(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> Field implBindField(IProvider0<R> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1> Field implBindField(IProvider1<R, V1> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2> Field implBindField(IProvider2<R, V1, V2> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> Field implBindField(IProvider3<R, V1, V2, V3> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> Field implBindField(IProvider4<R, V1, V2, V3, V4> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> Field implBindField(IProvider5<R, V1, V2, V3, V4, V5> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> Field implBindField(IProvider6<R, V1, V2, V3, V4, V5, V6> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> Field implBindField(IProvider7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> Field implBindField(IProvider8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implBindField(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> Field implBindField(IProvider9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implBindField(getSerializedLambda(fn));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Method implMethod(IConsumer0 fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1> Method implMethod(IConsumer1<V1> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2> Method implMethod(IConsumer2<V1, V2> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Method implMethod(IConsumer3<V1, V2, V3> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> Method implMethod(IConsumer4<V1, V2, V3, V4> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> Method implMethod(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> Method implMethod(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> Method implMethod(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> Method implMethod(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Method implMethod(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> Method implMethod(IProvider0<R> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1> Method implMethod(IProvider1<R, V1> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2> Method implMethod(IProvider2<R, V1, V2> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> Method implMethod(IProvider3<R, V1, V2, V3> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> Method implMethod(IProvider4<R, V1, V2, V3, V4> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> Method implMethod(IProvider5<R, V1, V2, V3, V4, V5> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> Method implMethod(IProvider6<R, V1, V2, V3, V4, V5, V6> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> Method implMethod(IProvider7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> Method implMethod(IProvider8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> Method implMethod(IProvider9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implMethod(getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String fieldName(IConsumer0 fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1> String fieldName(IConsumer1<V1> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2> String fieldName(IConsumer2<V1, V2> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String fieldName(IConsumer3<V1, V2, V3> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> String fieldName(IConsumer4<V1, V2, V3, V4> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> String fieldName(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> String fieldName(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> String fieldName(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> String fieldName(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> String fieldName(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> String fieldName(IProvider0<R> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1> String fieldName(IProvider1<R, V1> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2> String fieldName(IProvider2<R, V1, V2> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> String fieldName(IProvider3<R, V1, V2, V3> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> String fieldName(IProvider4<R, V1, V2, V3, V4> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> String fieldName(IProvider5<R, V1, V2, V3, V4, V5> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> String fieldName(IProvider6<R, V1, V2, V3, V4, V5, V6> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> String fieldName(IProvider7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> String fieldName(IProvider8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> String fieldName(IProvider9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return fieldName(getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String methodName(IConsumer0 fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1> String methodName(IConsumer1<V1> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2> String methodName(IConsumer2<V1, V2> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String methodName(IConsumer3<V1, V2, V3> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> String methodName(IConsumer4<V1, V2, V3, V4> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> String methodName(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> String methodName(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> String methodName(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> String methodName(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> String methodName(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return methodName(getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> String methodName(IProvider0<R> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1> String methodName(IProvider1<R, V1> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2> String methodName(IProvider2<R, V1, V2> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> String methodName(IProvider3<R, V1, V2, V3> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> String methodName(IProvider4<R, V1, V2, V3, V4> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> String methodName(IProvider5<R, V1, V2, V3, V4, V5> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> String methodName(IProvider6<R, V1, V2, V3, V4, V5, V6> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> String methodName(IProvider7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> String methodName(IProvider8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> String methodName(IProvider9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return methodName(getSerializedLambda(fn));
    }

    public static SerializedLambda getSerializedLambda(IConsumer0 fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1> SerializedLambda getSerializedLambda(IConsumer1<V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2> SerializedLambda getSerializedLambda(IConsumer2<V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3> SerializedLambda getSerializedLambda(IConsumer3<V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4> SerializedLambda getSerializedLambda(IConsumer4<V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6> SerializedLambda getSerializedLambda(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7> SerializedLambda getSerializedLambda(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> SerializedLambda getSerializedLambda(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> SerializedLambda getSerializedLambda(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return getFunctionSerializedLambda(fn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> SerializedLambda getSerializedLambda(IProvider0<R> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1> SerializedLambda getSerializedLambda(IProvider1<R, V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2> SerializedLambda getSerializedLambda(IProvider2<R, V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3> SerializedLambda getSerializedLambda(IProvider3<R, V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4> SerializedLambda getSerializedLambda(IProvider4<R, V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(IProvider5<R, V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6> SerializedLambda getSerializedLambda(IProvider6<R, V1, V2, V3, V4, V5, V6> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> SerializedLambda getSerializedLambda(IProvider7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> SerializedLambda getSerializedLambda(IProvider8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> SerializedLambda getSerializedLambda(IProvider9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return getFunctionSerializedLambda(fn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static SerializedLambda getFunctionSerializedLambda(IFunction fn) {
        return getSerializedLambdaNoExcept(fn);
    }

    public static SerializedLambda getSerializedLambdaNoExcept(Serializable fn) {
        try {
            return getSerializedLambda(fn);
        } catch (Exception e) {
            throw new BoostException(e.getMessage(), e);
        }
    }

    /**
     * 关键在于这个方法
     */
    public static SerializedLambda getSerializedLambda(Serializable fn) throws Exception {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
        return lambda;
    }
}
