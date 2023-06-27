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
public class ThrLambdaMethods {


    public static Method implMethod(IThrPredicate0 fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Method implMethod(IThrPredicate1<V1> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Method implMethod(IThrPredicate2<V1, V2> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Method implMethod(IThrPredicate3<V1, V2, V3> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> Method implMethod(IThrPredicate4<V1, V2, V3, V4> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> Method implMethod(IThrPredicate5<V1, V2, V3, V4, V5> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrBytePredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrCharPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrDoublePredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrFloatPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrIntPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrLongPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrShortPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Method implMethod(IThrConsumer0 fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Method implMethod(IThrConsumer1<V1> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Method implMethod(IThrConsumer2<V1, V2> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Method implMethod(IThrConsumer3<V1, V2, V3> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> Method implMethod(IThrConsumer4<V1, V2, V3, V4> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> Method implMethod(IThrConsumer5<V1, V2, V3, V4, V5> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> Method implMethod(IThrConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> Method implMethod(IThrConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> Method implMethod(IThrConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Method implMethod(IThrConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Method implMethod(IThrConsumer10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Method implMethod(IThrConsumer11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Method implMethod(IThrConsumer12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Method implMethod(IThrConsumer13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Method implMethod(IThrConsumer14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Method implMethod(IThrConsumer15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrBoolConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrByteConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrCharConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrDoubleConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrFloatConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrIntConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrLongConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrShortConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> Method implMethod(IThrSupplier0<R> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> Method implMethod(IThrSupplier1<R, V1> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> Method implMethod(IThrSupplier2<R, V1, V2> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> Method implMethod(IThrSupplier3<R, V1, V2, V3> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> Method implMethod(IThrSupplier4<R, V1, V2, V3, V4> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> Method implMethod(IThrSupplier5<R, V1, V2, V3, V4, V5> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> Method implMethod(IThrSupplier6<R, V1, V2, V3, V4, V5, V6> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> Method implMethod(IThrSupplier7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> Method implMethod(IThrSupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> Method implMethod(IThrSupplier9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Method implMethod(IThrSupplier10<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Method implMethod(IThrSupplier11<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Method implMethod(IThrSupplier12<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Method implMethod(IThrSupplier13<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Method implMethod(IThrSupplier14<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Method implMethod(IThrSupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrBoolSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrByteSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrCharSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrDoubleSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrFloatSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrIntSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrLongSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ThrShortSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
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
