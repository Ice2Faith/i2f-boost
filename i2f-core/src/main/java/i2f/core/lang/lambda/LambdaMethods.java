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
public class LambdaMethods {

    public static Method implMethod(IPredicate0 fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Method implMethod(IPredicate1<V1> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Method implMethod(IPredicate2<V1, V2> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Method implMethod(IPredicate3<V1, V2, V3> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> Method implMethod(IPredicate4<V1, V2, V3, V4> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> Method implMethod(IPredicate5<V1, V2, V3, V4, V5> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(BytePredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(CharPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(DoublePredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(FloatPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(IntPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(LongPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ShortPredicate fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Method implMethod(IConsumer0 fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1> Method implMethod(IConsumer1<V1> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2> Method implMethod(IConsumer2<V1, V2> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3> Method implMethod(IConsumer3<V1, V2, V3> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4> Method implMethod(IConsumer4<V1, V2, V3, V4> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5> Method implMethod(IConsumer5<V1, V2, V3, V4, V5> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6> Method implMethod(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> Method implMethod(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> Method implMethod(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Method implMethod(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Method implMethod(IConsumer10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Method implMethod(IConsumer11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Method implMethod(IConsumer12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Method implMethod(IConsumer13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Method implMethod(IConsumer14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Method implMethod(IConsumer15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(BoolConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ByteConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(CharConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(DoubleConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(FloatConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(IntConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(LongConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ShortConsumer fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> Method implMethod(ISupplier0<R> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1> Method implMethod(ISupplier1<R, V1> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2> Method implMethod(ISupplier2<R, V1, V2> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3> Method implMethod(ISupplier3<R, V1, V2, V3> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4> Method implMethod(ISupplier4<R, V1, V2, V3, V4> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5> Method implMethod(ISupplier5<R, V1, V2, V3, V4, V5> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6> Method implMethod(ISupplier6<R, V1, V2, V3, V4, V5, V6> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> Method implMethod(ISupplier7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> Method implMethod(ISupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> Method implMethod(ISupplier9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Method implMethod(ISupplier10<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Method implMethod(ISupplier11<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Method implMethod(ISupplier12<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Method implMethod(ISupplier13<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Method implMethod(ISupplier14<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Method implMethod(ISupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(BoolSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ByteSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(CharSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(DoubleSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(FloatSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(IntSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(LongSupplier fn) {
        return implMethod(LambdaFunctions.getSerializedLambda(fn));
    }

    public static Method implMethod(ShortSupplier fn) {
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
