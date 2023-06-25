package i2f.core.lang.lambda.impl;

import i2f.core.lang.functional.IFunction;
import i2f.core.lang.functional.consumer.*;
import i2f.core.lang.functional.predicate.*;
import i2f.core.lang.functional.supplier.*;

import java.lang.invoke.SerializedLambda;


public class LambdaFunctions {
    public static SerializedLambda getSerializedLambda(IPredicate0 fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1> SerializedLambda getSerializedLambda(IPredicate1<V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2> SerializedLambda getSerializedLambda(IPredicate2<V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3> SerializedLambda getSerializedLambda(IPredicate3<V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4> SerializedLambda getSerializedLambda(IPredicate4<V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(IPredicate5<V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> SerializedLambda getSerializedLambda(IConsumer10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> SerializedLambda getSerializedLambda(IConsumer11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> SerializedLambda getSerializedLambda(IConsumer12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> SerializedLambda getSerializedLambda(IConsumer13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> SerializedLambda getSerializedLambda(IConsumer14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> SerializedLambda getSerializedLambda(IConsumer15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return getFunctionSerializedLambda(fn);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> SerializedLambda getSerializedLambda(ISupplier0<R> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1> SerializedLambda getSerializedLambda(ISupplier1<R, V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2> SerializedLambda getSerializedLambda(ISupplier2<R, V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3> SerializedLambda getSerializedLambda(ISupplier3<R, V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4> SerializedLambda getSerializedLambda(ISupplier4<R, V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(ISupplier5<R, V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6> SerializedLambda getSerializedLambda(ISupplier6<R, V1, V2, V3, V4, V5, V6> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> SerializedLambda getSerializedLambda(ISupplier7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> SerializedLambda getSerializedLambda(ISupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> SerializedLambda getSerializedLambda(ISupplier9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> SerializedLambda getSerializedLambda(ISupplier10<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> SerializedLambda getSerializedLambda(ISupplier11<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> SerializedLambda getSerializedLambda(ISupplier12<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> SerializedLambda getSerializedLambda(ISupplier13<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> SerializedLambda getSerializedLambda(ISupplier14<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> SerializedLambda getSerializedLambda(ISupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return getFunctionSerializedLambda(fn);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static SerializedLambda getFunctionSerializedLambda(IFunction fn) {
        return LambdaInflater.getSerializedLambdaNoExcept(fn);
    }

}
