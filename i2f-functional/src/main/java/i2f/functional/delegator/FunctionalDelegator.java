package i2f.functional.delegator;

import i2f.functional.IFunctional;
import i2f.functional.array.*;
import i2f.functional.array.bools.*;
import i2f.functional.array.bytes.*;
import i2f.functional.array.chars.*;
import i2f.functional.array.doubles.*;
import i2f.functional.array.floats.*;
import i2f.functional.array.ints.*;
import i2f.functional.array.longs.*;
import i2f.functional.array.objs.*;
import i2f.functional.array.shorts.*;
import i2f.functional.base.*;
import i2f.functional.base.bytes.*;
import i2f.functional.base.chars.*;
import i2f.functional.base.doubles.*;
import i2f.functional.base.floats.*;
import i2f.functional.base.longs.*;
import i2f.functional.base.shorts.*;
import i2f.functional.comparator.IComparator;
import i2f.functional.comparator.impl.*;
import i2f.functional.consumer.IConsumer;
import i2f.functional.consumer.impl.*;
import i2f.functional.func.IFunction;
import i2f.functional.func.impl.*;
import i2f.functional.predicate.IPredicate;
import i2f.functional.predicate.impl.*;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:50
 * @desc
 */
public class FunctionalDelegator<U> {

    protected Function<IFunctional,U> delegator;

    public FunctionalDelegator(Function<IFunctional, U> delegator) {
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

    public U delegate(IConsumer0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IConsumer1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IConsumer2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IConsumer3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IConsumer4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IConsumer5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U delegate(IConsumer6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U delegate(IConsumer7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IConsumer8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IConsumer9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IConsumer10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
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

    public U delegate(IComparator0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IComparator1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IComparator2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IComparator3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IComparator4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IComparator5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U delegate(IComparator6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U delegate(IComparator7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IComparator8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IComparator9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IComparator10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
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

    public U delegate(IPredicate0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IPredicate1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IPredicate2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IPredicate3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IPredicate4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IPredicate5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U delegate(IPredicate6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U delegate(IPredicate7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IPredicate8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IPredicate9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IPredicate10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
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

    public<R> U delegate(IFunction0<R> val){
        return delegator.apply(val);
    }

    public<R,V1> U delegate(IFunction1<R,V1> val){
        return delegator.apply(val);
    }

    public<R,V1,V2> U delegate(IFunction2<R,V1,V2> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3> U delegate(IFunction3<R,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4> U delegate(IFunction4<R,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5> U delegate(IFunction5<R,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6> U delegate(IFunction6<R,V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7> U delegate(IFunction7<R,V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8> U delegate(IFunction8<R,V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> U delegate(IFunction9<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U delegate(IFunction10<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U delegate(ICharFunction val){
        return delegator.apply(val);
    }

    public U delegate(ICharFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(ICharFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(ICharFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(ICharFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(ICharFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(ICharFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IByteFunction val){
        return delegator.apply(val);
    }

    public U delegate(IByteFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IByteFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IByteFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IByteFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IByteFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IByteFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IShortFunction val){
        return delegator.apply(val);
    }

    public U delegate(IShortFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IShortFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IShortFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IShortFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IShortFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IShortFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(ILongFunction val){
        return delegator.apply(val);
    }

    public U delegate(ILongFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(ILongFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(ILongFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(ILongFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(ILongFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(ILongFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IFloatFunction val){
        return delegator.apply(val);
    }

    public U delegate(IFloatFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IFloatFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IFloatFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IFloatFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IFloatFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IFloatFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IDoubleFunction val){
        return delegator.apply(val);
    }

    public U delegate(IDoubleFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IDoubleFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IDoubleFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IDoubleFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IDoubleFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IDoubleFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U delegate(IArrayFunction val){
        return delegator.apply(val);
    }



    public U delegate(IIntArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IIntArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IIntArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IIntArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IIntArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IIntArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IIntArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IObjectArrayFunction val){
        return delegator.apply(val);
    }

    public<T> U delegate(IObjectArrayFunction0<T> val){
        return delegator.apply(val);
    }

    public<T,V1> U delegate(IObjectArrayFunction1<T,V1> val){
        return delegator.apply(val);
    }

    public<T,V1,V2> U delegate(IObjectArrayFunction2<T,V1,V2> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3> U delegate(IObjectArrayFunction3<T,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4> U delegate(IObjectArrayFunction4<T,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4,V5> U delegate(IObjectArrayFunction5<T,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U delegate(ILongArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(ILongArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(ILongArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(ILongArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(ILongArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(ILongArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(ILongArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IDoubleArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IDoubleArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IDoubleArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IDoubleArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IDoubleArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IDoubleArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IDoubleArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(ICharArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(ICharArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(ICharArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(ICharArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(ICharArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(ICharArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(ICharArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IBoolArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IBoolArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IBoolArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IBoolArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IBoolArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IBoolArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IBoolArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U delegate(IShortArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IShortArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IShortArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IShortArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IShortArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IShortArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IShortArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U delegate(IByteArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IByteArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IByteArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IByteArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IByteArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IByteArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IByteArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U delegate(IFloatArrayFunction val){
        return delegator.apply(val);
    }

    public U delegate(IFloatArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U delegate(IFloatArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U delegate(IFloatArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U delegate(IFloatArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U delegate(IFloatArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U delegate(IFloatArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

}
