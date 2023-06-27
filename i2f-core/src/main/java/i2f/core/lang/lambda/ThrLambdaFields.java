package i2f.core.lang.lambda;

import i2f.core.lang.functional.thr.consumer.*;
import i2f.core.lang.functional.thr.predicate.*;
import i2f.core.lang.functional.thr.supplier.*;
import i2f.core.lang.lambda.impl.LambdaFunctions;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/6/27 8:56
 * @desc
 */
public class ThrLambdaFields {

    public static Field implBindField(IThrPredicate0 fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Field implBindField(IThrPredicate1<V1> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Field implBindField(IThrPredicate2<V1, V2> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Field implBindField(IThrPredicate3<V1, V2, V3> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrBytePredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrCharPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrDoublePredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrFloatPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrIntPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrLongPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrShortPredicate fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Field implBindField(IThrConsumer0 fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Field implBindField(IThrConsumer1<V1> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Field implBindField(IThrConsumer2<V1, V2> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Field implBindField(IThrConsumer3<V1, V2, V3> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrBoolConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrByteConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrCharConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrDoubleConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrFloatConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrIntConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrLongConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrShortConsumer fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> Field implBindField(IThrSupplier0<R> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> Field implBindField(IThrSupplier1<R, V1> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> Field implBindField(IThrSupplier2<R, V1, V2> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> Field implBindField(IThrSupplier3<R, V1, V2, V3> fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrBoolSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrByteSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrCharSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrDoubleSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrFloatSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrIntSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrLongSupplier fn) {
        return implBindField(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Field implBindField(ThrShortSupplier fn) {
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
