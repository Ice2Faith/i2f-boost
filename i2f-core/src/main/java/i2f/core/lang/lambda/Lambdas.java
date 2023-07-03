package i2f.core.lang.lambda;

import i2f.core.lang.functional.consumer.*;
import i2f.core.lang.functional.predicate.*;
import i2f.core.lang.functional.supplier.*;
import i2f.core.lang.lambda.impl.LambdaFunctions;
import i2f.core.reflection.reflect.Reflects;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class Lambdas {


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String fieldName(IPredicate0 fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String fieldName(IPredicate1<V1> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String fieldName(IPredicate2<V1, V2> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String fieldName(IPredicate3<V1, V2, V3> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(BytePredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(CharPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(DoublePredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(FloatPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(IntPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(LongPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ShortPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String fieldName(IConsumer0 fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String fieldName(IConsumer1<V1> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String fieldName(IConsumer2<V1, V2> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String fieldName(IConsumer3<V1, V2, V3> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(BoolConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ByteConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(CharConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(DoubleConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(FloatConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(IntConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(LongConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ShortConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> String fieldName(ISupplier0<R> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> String fieldName(ISupplier1<R, V1> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> String fieldName(ISupplier2<R, V1, V2> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> String fieldName(ISupplier3<R, V1, V2, V3> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(BoolSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ByteSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(CharSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(DoubleSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(FloatSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(IntSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(LongSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ShortSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String methodName(IPredicate0 fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String methodName(IPredicate1<V1> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String methodName(IPredicate2<V1, V2> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String methodName(IPredicate3<V1, V2, V3> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> String methodName(IPredicate4<V1, V2, V3, V4> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> String methodName(IPredicate5<V1, V2, V3, V4, V5> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(BytePredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(CharPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(DoublePredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(FloatPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(IntPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(LongPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ShortPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String methodName(IConsumer0 fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String methodName(IConsumer1<V1> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String methodName(IConsumer2<V1, V2> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String methodName(IConsumer3<V1, V2, V3> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> String methodName(IConsumer4<V1, V2, V3, V4> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> String methodName(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(BoolConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ByteConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(CharConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(DoubleConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(FloatConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(IntConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(LongConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ShortConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> String methodName(ISupplier0<R> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> String methodName(ISupplier1<R, V1> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> String methodName(ISupplier2<R, V1, V2> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> String methodName(ISupplier3<R, V1, V2, V3> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> String methodName(ISupplier4<R, V1, V2, V3, V4> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> String methodName(ISupplier5<R, V1, V2, V3, V4, V5> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(BoolSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ByteSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(CharSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(DoubleSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(FloatSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(IntSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(LongSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ShortSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Method implMethod(SerializedLambda lambda) {
        try {
            String implClassName = Reflects.path2ClassName(lambda.getImplClass());
            Class implClass = Reflects.findClass(implClassName);
            Method implMethod = Reflects.methodOfSignature(implClass,
                    lambda.getImplMethodName(),
                    lambda.getImplMethodSignature());
            return implMethod;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Field implBindField(SerializedLambda lambda) {
        try {
            String implClassName = Reflects.path2ClassName(lambda.getImplClass());
            Class implClass = Reflects.findClass(implClassName);
            String fieldName = Reflects.fieldNameByMethodName(lambda.getImplMethodName());
            return Reflects.field(implClass, fieldName, true);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String methodName(SerializedLambda lambda) {
        return lambda.getImplMethodName();
    }

    public static String fieldName(SerializedLambda lambda) {
        return Reflects.fieldNameByMethodName(lambda.getImplMethodName());
    }

}
