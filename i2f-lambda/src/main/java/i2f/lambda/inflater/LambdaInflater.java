package i2f.lambda.inflater;

import i2f.lambda.util.ReflectSignature;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/3/29 16:38
 * @desc
 */
public class LambdaInflater {


    public static Field getSerializedLambdaFieldNullable(Object obj){
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if(lambda==null){
            return null;
        }
        String clazzName = ReflectSignature.path2ClassName(lambda.getImplClass());
        String methodName = lambda.getImplMethodName();
        Class<?> clazz = ReflectSignature.findClass(clazzName);
        if(clazz==null){
            return null;
        }
        String fieldName = ReflectSignature.fieldNameByMethodName(methodName);
        Set<Field> set = ReflectSignature.getFields(clazz, (field) -> {
            if(field.getName().equals(methodName)){
                return true;
            }
            return false;
        }, true);
        if(!set.isEmpty()){
            return set.iterator().next();
        }
        return null;
    }

    public static Method getSerializedLambdaMethodNullable(Object obj){
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if(lambda==null){
            return null;
        }
        String clazzName = ReflectSignature.path2ClassName(lambda.getImplClass());
        String methodName = lambda.getImplMethodName();
        String methodSignature = lambda.getImplMethodSignature();
        Class<?> clazz = ReflectSignature.findClass(clazzName);
        if(clazz==null){
            return null;
        }
        Set<Method> set = ReflectSignature.getMethods(clazz, (method) -> {
            if(method.getName().equals(methodName)){
                String sign = ReflectSignature.sign(method);
                if(sign.equals(methodSignature)){
                    return true;
                }
            }
            return false;
        }, true);
        if(!set.isEmpty()){
            return set.iterator().next();
        }
        return null;
    }

    public static Class<?> getSerializedLambdaClassNullable(Object obj){
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if(lambda==null){
            return null;
        }
        String clazzName = ReflectSignature.path2ClassName(lambda.getImplClass());
        return ReflectSignature.findClass(clazzName);
    }


    public static SerializedLambda getSerializedLambdaNullable(Object obj){
        if(!(obj instanceof Serializable)){
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
