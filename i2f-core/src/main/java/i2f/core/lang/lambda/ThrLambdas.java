package i2f.core.lang.lambda;

import i2f.core.lang.functional.thr.consumer.*;
import i2f.core.lang.functional.thr.predicate.*;
import i2f.core.lang.functional.thr.supplier.*;
import i2f.core.lang.lambda.impl.LambdaFunctions;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ThrLambdas {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String fieldName(IThrPredicate0 fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String fieldName(IThrPredicate1<V1> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String fieldName(IThrPredicate2<V1, V2> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String fieldName(IThrPredicate3<V1, V2, V3> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrBytePredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrCharPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrDoublePredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrFloatPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrIntPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrLongPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrShortPredicate fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String fieldName(IThrConsumer0 fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String fieldName(IThrConsumer1<V1> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String fieldName(IThrConsumer2<V1, V2> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String fieldName(IThrConsumer3<V1, V2, V3> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrBoolConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrByteConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrCharConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrDoubleConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrFloatConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrIntConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrLongConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrShortConsumer fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> String fieldName(IThrSupplier0<R> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> String fieldName(IThrSupplier1<R, V1> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> String fieldName(IThrSupplier2<R, V1, V2> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> String fieldName(IThrSupplier3<R, V1, V2, V3> fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrBoolSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrByteSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrCharSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrDoubleSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrFloatSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrIntSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrLongSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String fieldName(ThrShortSupplier fn) {
        return fieldName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String methodName(IThrPredicate0 fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String methodName(IThrPredicate1<V1> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String methodName(IThrPredicate2<V1, V2> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String methodName(IThrPredicate3<V1, V2, V3> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> String methodName(IThrPredicate4<V1, V2, V3, V4> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> String methodName(IThrPredicate5<V1, V2, V3, V4, V5> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrBytePredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrCharPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrDoublePredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrFloatPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrIntPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrLongPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrShortPredicate fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String methodName(IThrConsumer0 fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> String methodName(IThrConsumer1<V1> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> String methodName(IThrConsumer2<V1, V2> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> String methodName(IThrConsumer3<V1, V2, V3> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> String methodName(IThrConsumer4<V1, V2, V3, V4> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> String methodName(IThrConsumer5<V1, V2, V3, V4, V5> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> String methodName(IThrConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> String methodName(IThrConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> String methodName(IThrConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> String methodName(IThrConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> String methodName(IThrConsumer10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> String methodName(IThrConsumer11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> String methodName(IThrConsumer12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> String methodName(IThrConsumer13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> String methodName(IThrConsumer14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> String methodName(IThrConsumer15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrBoolConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrByteConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrCharConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrDoubleConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrFloatConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrIntConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrLongConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrShortConsumer fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> String methodName(IThrSupplier0<R> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> String methodName(IThrSupplier1<R, V1> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> String methodName(IThrSupplier2<R, V1, V2> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> String methodName(IThrSupplier3<R, V1, V2, V3> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> String methodName(IThrSupplier4<R, V1, V2, V3, V4> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> String methodName(IThrSupplier5<R, V1, V2, V3, V4, V5> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> String methodName(IThrSupplier6<R, V1, V2, V3, V4, V5, V6> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> String methodName(IThrSupplier7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> String methodName(IThrSupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> String methodName(IThrSupplier9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> String methodName(IThrSupplier10<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> String methodName(IThrSupplier11<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> String methodName(IThrSupplier12<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> String methodName(IThrSupplier13<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> String methodName(IThrSupplier14<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> String methodName(IThrSupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrBoolSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrByteSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrCharSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrDoubleSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrFloatSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrIntSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrLongSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static String methodName(ThrShortSupplier fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
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