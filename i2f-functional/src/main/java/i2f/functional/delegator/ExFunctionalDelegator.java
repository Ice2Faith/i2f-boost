package i2f.functional.delegator;

import i2f.functional.IFunctional;
import i2f.functional.array.*;
import i2f.functional.array.bools.*;
import i2f.functional.array.bools.except.IExBoolArrayFunction;
import i2f.functional.array.bools.except.impl.*;
import i2f.functional.array.bytes.*;
import i2f.functional.array.bytes.except.IExByteArrayFunction;
import i2f.functional.array.bytes.except.impl.*;
import i2f.functional.array.chars.*;
import i2f.functional.array.chars.except.IExCharArrayFunction;
import i2f.functional.array.chars.except.impl.*;
import i2f.functional.array.doubles.*;
import i2f.functional.array.doubles.except.IExDoubleArrayFunction;
import i2f.functional.array.doubles.except.impl.*;
import i2f.functional.array.floats.*;
import i2f.functional.array.floats.except.IExFloatArrayFunction;
import i2f.functional.array.floats.except.impl.*;
import i2f.functional.array.ints.*;
import i2f.functional.array.ints.except.IExIntArrayFunction;
import i2f.functional.array.ints.except.impl.*;
import i2f.functional.array.longs.*;
import i2f.functional.array.longs.except.IExLongArrayFunction;
import i2f.functional.array.longs.except.impl.*;
import i2f.functional.array.objs.*;
import i2f.functional.array.objs.except.IExObjectArrayFunction;
import i2f.functional.array.objs.except.impl.*;
import i2f.functional.array.shorts.*;
import i2f.functional.array.shorts.except.IExShortArrayFunction;
import i2f.functional.array.shorts.except.impl.*;
import i2f.functional.base.*;
import i2f.functional.base.bytes.*;
import i2f.functional.base.bytes.except.IExByteFunction;
import i2f.functional.base.bytes.except.impl.*;
import i2f.functional.base.chars.*;
import i2f.functional.base.chars.except.IExCharFunction;
import i2f.functional.base.chars.except.impl.*;
import i2f.functional.base.doubles.*;
import i2f.functional.base.doubles.except.IExDoubleFunction;
import i2f.functional.base.doubles.except.impl.*;
import i2f.functional.base.floats.*;
import i2f.functional.base.floats.except.IExFloatFunction;
import i2f.functional.base.floats.except.impl.*;
import i2f.functional.base.longs.*;
import i2f.functional.base.longs.except.IExLongFunction;
import i2f.functional.base.longs.except.impl.*;
import i2f.functional.base.shorts.*;
import i2f.functional.base.shorts.except.IExShortFunction;
import i2f.functional.base.shorts.except.impl.*;
import i2f.functional.comparator.IComparator;
import i2f.functional.comparator.except.IExComparator;
import i2f.functional.comparator.except.impl.*;
import i2f.functional.comparator.impl.*;
import i2f.functional.consumer.IConsumer;
import i2f.functional.consumer.except.IExConsumer;
import i2f.functional.consumer.except.impl.*;
import i2f.functional.consumer.impl.*;
import i2f.functional.func.IFunction;
import i2f.functional.func.except.IExFunction;
import i2f.functional.func.except.impl.*;
import i2f.functional.func.impl.*;
import i2f.functional.predicate.IPredicate;
import i2f.functional.predicate.except.IExPredicate;
import i2f.functional.predicate.except.impl.*;
import i2f.functional.predicate.impl.*;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:50
 * @desc
 */
public class ExFunctionalDelegator<U> {

    protected Function<IFunctional,U> delegator;

    public ExFunctionalDelegator(Function<IFunctional, U> delegator) {
        this.delegator = delegator;
    }

    public U delegate(IFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(IVoidFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(IConsumer val){
        return delegator.apply(val);
    }

    public U delegate(IExConsumer val){
        return delegator.apply(val);
    }

    public U delegate(IExConsumer0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExConsumer1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExConsumer2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExConsumer3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExConsumer4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExConsumer5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U delegate(IExConsumer6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U delegate(IExConsumer7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IExConsumer8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IExConsumer9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IExConsumer10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(IIntFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(IComparator val){
        return delegator.apply(val);
    }

    public U delegate(IExComparator val){
        return delegator.apply(val);
    }

    public U delegate(IExComparator0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExComparator1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExComparator2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExComparator3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExComparator4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExComparator5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U delegate(IExComparator6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U delegate(IExComparator7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IExComparator8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IExComparator9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IExComparator10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IBoolFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(IPredicate val){
        return delegator.apply(val);
    }

    public U delegate(IExPredicate val){
        return delegator.apply(val);
    }

    public U delegate(IExPredicate0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExPredicate1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExPredicate2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExPredicate3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExPredicate4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExPredicate5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U delegate(IExPredicate6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U delegate(IExPredicate7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IExPredicate8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IExPredicate9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IExPredicate10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IObjectFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(IFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExFunction val){
        return delegator.apply(val);
    }

    public<R> U delegate(IExFunction0<R> val){
        return delegator.apply(val);
    }

    public<R,V1> U delegate(IExFunction1<R,V1> val){
        return delegator.apply(val);
    }

    public<R,V1,V2> U delegate(IExFunction2<R,V1,V2> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3> U delegate(IExFunction3<R,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4> U delegate(IExFunction4<R,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5> U delegate(IExFunction5<R,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6> U delegate(IExFunction6<R,V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7> U delegate(IExFunction7<R,V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IExFunction8<R,V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IExFunction9<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IExFunction10<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U delegate(ICharFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExCharFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExCharFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExCharFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExCharFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExCharFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExCharFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExCharFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IByteFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExByteFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExByteFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExByteFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExByteFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExByteFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExByteFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExByteFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IShortFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExShortFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExShortFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExShortFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExShortFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExShortFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExShortFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExShortFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(ILongFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExLongFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExLongFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExLongFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExLongFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExLongFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExLongFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExLongFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IFloatFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExFloatFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExFloatFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExFloatFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExFloatFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExFloatFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExFloatFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExFloatFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IDoubleFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExDoubleFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExDoubleFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExDoubleFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExDoubleFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExDoubleFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExDoubleFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExDoubleFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////


    public U delegate(IArrayFunction val){
        return delegator.apply(val);
    }



    public U delegate(IIntArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExIntArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExIntArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExIntArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExIntArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExIntArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExIntArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExIntArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IObjectArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExObjectArrayFunction val){
        return delegator.apply(val);
    }

    public<T> U delegate(IExObjectArrayFunction0<T> val){
        return delegator.apply(val);
    }

    public<T,V1> U delegate(IExObjectArrayFunction1<T,V1> val){
        return delegator.apply(val);
    }

    public<T,V1,V2> U delegate(IExObjectArrayFunction2<T,V1,V2> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3> U delegate(IExObjectArrayFunction3<T,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4> U delegate(IExObjectArrayFunction4<T,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4,V5> U delegate(IExObjectArrayFunction5<T,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(ILongArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExLongArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExLongArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExLongArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExLongArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExLongArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExLongArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExLongArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IDoubleArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExDoubleArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExDoubleArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExDoubleArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExDoubleArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExDoubleArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExDoubleArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExDoubleArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(ICharArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExCharArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExCharArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExCharArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExCharArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExCharArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExCharArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExCharArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IBoolArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExBoolArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExBoolArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExBoolArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExBoolArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExBoolArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExBoolArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExBoolArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IShortArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExShortArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExShortArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExShortArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExShortArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExShortArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExShortArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExShortArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IByteArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExByteArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExByteArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExByteArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExByteArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExByteArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExByteArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExByteArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////





    public U delegate(IFloatArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExFloatArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IExFloatArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IExFloatArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IExFloatArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IExFloatArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IExFloatArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IExFloatArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////

}
