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

    public static <V1, V2, V3, V4, V5, V6> String methodName(IConsumer6<V1, V2, V3, V4, V5, V6> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7> String methodName(IConsumer7<V1, V2, V3, V4, V5, V6, V7> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8> String methodName(IConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9> String methodName(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> String methodName(IConsumer10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> String methodName(IConsumer11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> String methodName(IConsumer12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> String methodName(IConsumer13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> String methodName(IConsumer14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> String methodName(IConsumer15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
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

    public static <R, V1, V2, V3, V4, V5, V6> String methodName(ISupplier6<R, V1, V2, V3, V4, V5, V6> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7> String methodName(ISupplier7<R, V1, V2, V3, V4, V5, V6, V7> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8> String methodName(ISupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> String methodName(ISupplier9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> String methodName(ISupplier10<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> String methodName(ISupplier11<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> String methodName(ISupplier12<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> String methodName(ISupplier13<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> String methodName(ISupplier14<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> fn) {
        return methodName(LambdaFunctions.getSerializedLambda(fn));
    }

    public static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> String methodName(ISupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> fn) {
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
