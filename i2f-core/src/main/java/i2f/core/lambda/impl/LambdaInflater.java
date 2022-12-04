package i2f.core.lambda.impl;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;


public class LambdaInflater {

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
