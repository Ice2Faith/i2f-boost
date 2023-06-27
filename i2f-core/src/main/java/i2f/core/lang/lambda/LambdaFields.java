package i2f.core.lang.lambda;

import i2f.core.lang.functional.consumer.*;
import i2f.core.lang.functional.predicate.*;
import i2f.core.lang.functional.supplier.*;
import i2f.core.lang.lambda.impl.LambdaFunctions;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/6/27 8:56
 * @desc
 */
public class LambdaFields {

    public static Field implBindField(IPredicate0 fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Field implBindField(IPredicate1<V1> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Field implBindField(IPredicate2<V1, V2> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Field implBindField(IPredicate3<V1, V2, V3> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(BytePredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(CharPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(DoublePredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(FloatPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(IntPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(LongPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ShortPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Field implBindField(IConsumer0 fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Field implBindField(IConsumer1<V1> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Field implBindField(IConsumer2<V1, V2> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Field implBindField(IConsumer3<V1, V2, V3> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(BoolConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ByteConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(CharConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(DoubleConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(FloatConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(IntConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(LongConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ShortConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> Field implBindField(ISupplier0<R> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> Field implBindField(ISupplier1<R, V1> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> Field implBindField(ISupplier2<R, V1, V2> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> Field implBindField(ISupplier3<R, V1, V2, V3> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(BoolSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ByteSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(CharSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(DoubleSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(FloatSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(IntSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(LongSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ShortSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Method implMethod(SerializedLambda lambda) {
        return Lambdas.implMethod(lambda);
    }

    public static Field implBindField(SerializedLambda lambda) {
        return Lambdas.implBindField(lambda);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String methodName(SerializedLambda lambda) {
        return Lambdas.methodName(lambda);
    }

    public static String fieldName(SerializedLambda lambda) {
        return Lambdas.fieldName(lambda);
    }
}
