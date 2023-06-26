package i2f.core.lang.lambda.impl;

import i2f.core.lang.functional.IFunction;
import i2f.core.lang.functional.consumer.*;
import i2f.core.lang.functional.predicate.*;
import i2f.core.lang.functional.supplier.*;
import i2f.core.lang.functional.thr.consumer.*;
import i2f.core.lang.functional.thr.predicate.*;
import i2f.core.lang.functional.thr.supplier.*;

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

    public static SerializedLambda getSerializedLambda(BytePredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(CharPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(DoublePredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(FloatPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(IntPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(LongPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ShortPredicate fn) {
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

    public static SerializedLambda getSerializedLambda(BoolConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ByteConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(CharConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(DoubleConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(FloatConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(IntConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(LongConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ShortConsumer fn) {
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

    public static SerializedLambda getSerializedLambda(BoolSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ByteSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(CharSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(DoubleSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(FloatSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(IntSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(LongSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ShortSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static SerializedLambda getSerializedLambda(IThrPredicate0 fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1> SerializedLambda getSerializedLambda(IThrPredicate1<V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2> SerializedLambda getSerializedLambda(IThrPredicate2<V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3> SerializedLambda getSerializedLambda(IThrPredicate3<V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4> SerializedLambda getSerializedLambda(IThrPredicate4<V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(IThrPredicate5<V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrBytePredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrCharPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrDoublePredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrFloatPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrIntPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrLongPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrShortPredicate fn) {
        return getFunctionSerializedLambda(fn);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static SerializedLambda getSerializedLambda(IThrConsumer0 fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1> SerializedLambda getSerializedLambda(IThrConsumer1<V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2> SerializedLambda getSerializedLambda(IThrConsumer2<V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3> SerializedLambda getSerializedLambda(IThrConsumer3<V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4> SerializedLambda getSerializedLambda(IThrConsumer4<V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(IThrConsumer5<V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6> SerializedLambda getSerializedLambda(IThrConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7> SerializedLambda getSerializedLambda(IThrConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> SerializedLambda getSerializedLambda(IThrConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> SerializedLambda getSerializedLambda(IThrConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> SerializedLambda getSerializedLambda(IThrConsumer10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> SerializedLambda getSerializedLambda(IThrConsumer11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> SerializedLambda getSerializedLambda(IThrConsumer12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> SerializedLambda getSerializedLambda(IThrConsumer13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> SerializedLambda getSerializedLambda(IThrConsumer14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> SerializedLambda getSerializedLambda(IThrConsumer15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrBoolConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrByteConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrCharConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrDoubleConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrFloatConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrIntConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrLongConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrShortConsumer fn) {
        return getFunctionSerializedLambda(fn);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static <R> SerializedLambda getSerializedLambda(IThrSupplier0<R> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1> SerializedLambda getSerializedLambda(IThrSupplier1<R, V1> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2> SerializedLambda getSerializedLambda(IThrSupplier2<R, V1, V2> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3> SerializedLambda getSerializedLambda(IThrSupplier3<R, V1, V2, V3> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4> SerializedLambda getSerializedLambda(IThrSupplier4<R, V1, V2, V3, V4> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5> SerializedLambda getSerializedLambda(IThrSupplier5<R, V1, V2, V3, V4, V5> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6> SerializedLambda getSerializedLambda(IThrSupplier6<R, V1, V2, V3, V4, V5, V6> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> SerializedLambda getSerializedLambda(IThrSupplier7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> SerializedLambda getSerializedLambda(IThrSupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> SerializedLambda getSerializedLambda(IThrSupplier9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> SerializedLambda getSerializedLambda(IThrSupplier10<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> SerializedLambda getSerializedLambda(IThrSupplier11<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> SerializedLambda getSerializedLambda(IThrSupplier12<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> SerializedLambda getSerializedLambda(IThrSupplier13<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> SerializedLambda getSerializedLambda(IThrSupplier14<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> SerializedLambda getSerializedLambda(IThrSupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrBoolSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrByteSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrCharSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrDoubleSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrFloatSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrIntSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrLongSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }

    public static SerializedLambda getSerializedLambda(ThrShortSupplier fn) {
        return getFunctionSerializedLambda(fn);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static SerializedLambda getFunctionSerializedLambda(IFunction fn) {
        return LambdaInflater.getSerializedLambdaNoExcept(fn);
    }

}
