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

    public U get(IFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(IVoidFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(IConsumer val){
        return delegator.apply(val);
    }

    public U get(IExConsumer val){
        return delegator.apply(val);
    }

    public U get(IExConsumer0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExConsumer1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExConsumer2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExConsumer3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExConsumer4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExConsumer5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U get(IExConsumer6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U get(IExConsumer7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U get(IExConsumer8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IExConsumer9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IExConsumer10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(IIntFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(IComparator val){
        return delegator.apply(val);
    }

    public U get(IExComparator val){
        return delegator.apply(val);
    }

    public U get(IExComparator0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExComparator1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExComparator2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExComparator3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExComparator4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExComparator5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U get(IExComparator6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U get(IExComparator7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U get(IExComparator8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IExComparator9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IExComparator10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U get(IBoolFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(IPredicate val){
        return delegator.apply(val);
    }

    public U get(IExPredicate val){
        return delegator.apply(val);
    }

    public U get(IExPredicate0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExPredicate1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExPredicate2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExPredicate3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExPredicate4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExPredicate5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U get(IExPredicate6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U get(IExPredicate7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U get(IExPredicate8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IExPredicate9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IExPredicate10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(IObjectFunctional val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(IFunction val){
        return delegator.apply(val);
    }

    public U get(IExFunction val){
        return delegator.apply(val);
    }

    public<R> U get(IExFunction0<R> val){
        return delegator.apply(val);
    }

    public<R,V1> U get(IExFunction1<R,V1> val){
        return delegator.apply(val);
    }

    public<R,V1,V2> U get(IExFunction2<R,V1,V2> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3> U get(IExFunction3<R,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4> U get(IExFunction4<R,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5> U get(IExFunction5<R,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6> U get(IExFunction6<R,V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7> U get(IExFunction7<R,V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8> U get(IExFunction8<R,V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IExFunction9<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IExFunction10<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U get(ICharFunction val){
        return delegator.apply(val);
    }

    public U get(IExCharFunction val){
        return delegator.apply(val);
    }

    public U get(IExCharFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExCharFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExCharFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExCharFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExCharFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExCharFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IByteFunction val){
        return delegator.apply(val);
    }

    public U get(IExByteFunction val){
        return delegator.apply(val);
    }

    public U get(IExByteFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExByteFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExByteFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExByteFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExByteFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExByteFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IShortFunction val){
        return delegator.apply(val);
    }

    public U get(IExShortFunction val){
        return delegator.apply(val);
    }

    public U get(IExShortFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExShortFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExShortFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExShortFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExShortFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExShortFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(ILongFunction val){
        return delegator.apply(val);
    }

    public U get(IExLongFunction val){
        return delegator.apply(val);
    }

    public U get(IExLongFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExLongFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExLongFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExLongFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExLongFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExLongFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IFloatFunction val){
        return delegator.apply(val);
    }

    public U get(IExFloatFunction val){
        return delegator.apply(val);
    }

    public U get(IExFloatFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExFloatFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExFloatFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExFloatFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExFloatFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExFloatFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IDoubleFunction val){
        return delegator.apply(val);
    }

    public U get(IExDoubleFunction val){
        return delegator.apply(val);
    }

    public U get(IExDoubleFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExDoubleFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExDoubleFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExDoubleFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExDoubleFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExDoubleFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////


    public U get(IArrayFunction val){
        return delegator.apply(val);
    }



    public U get(IIntArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExIntArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExIntArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExIntArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExIntArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExIntArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExIntArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExIntArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////



    public U get(IObjectArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExObjectArrayFunction val){
        return delegator.apply(val);
    }

    public<T> U get(IExObjectArrayFunction0<T> val){
        return delegator.apply(val);
    }

    public<T,V1> U get(IExObjectArrayFunction1<T,V1> val){
        return delegator.apply(val);
    }

    public<T,V1,V2> U get(IExObjectArrayFunction2<T,V1,V2> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3> U get(IExObjectArrayFunction3<T,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4> U get(IExObjectArrayFunction4<T,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4,V5> U get(IExObjectArrayFunction5<T,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////

    public U get(ILongArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExLongArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExLongArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExLongArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExLongArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExLongArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExLongArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExLongArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////



    public U get(IDoubleArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExDoubleArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExDoubleArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExDoubleArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExDoubleArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExDoubleArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExDoubleArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExDoubleArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(ICharArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExCharArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExCharArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExCharArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExCharArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExCharArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExCharArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExCharArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IBoolArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExBoolArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExBoolArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExBoolArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExBoolArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExBoolArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExBoolArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExBoolArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IShortArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExShortArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExShortArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExShortArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExShortArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExShortArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExShortArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExShortArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////




    public U get(IByteArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExByteArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExByteArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExByteArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExByteArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExByteArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExByteArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExByteArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////





    public U get(IFloatArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExFloatArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IExFloatArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IExFloatArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IExFloatArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IExFloatArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IExFloatArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IExFloatArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }


    //////////////////////////////////////////////////////////////////////////////////

}
