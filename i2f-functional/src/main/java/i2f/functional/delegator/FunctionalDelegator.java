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

    public U get(IConsumer0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IConsumer1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IConsumer2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IConsumer3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IConsumer4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IConsumer5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U get(IConsumer6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U get(IConsumer7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U get(IConsumer8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IConsumer9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IConsumer10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
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

    public U get(IComparator0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IComparator1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IComparator2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IComparator3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IComparator4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IComparator5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U get(IComparator6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U get(IComparator7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U get(IComparator8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IComparator9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IComparator10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
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

    public U get(IPredicate0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IPredicate1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IPredicate2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IPredicate3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IPredicate4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IPredicate5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6> U get(IPredicate6<V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7> U get(IPredicate7<V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8> U get(IPredicate8<V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IPredicate9<V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IPredicate10<V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
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

    public<R> U get(IFunction0<R> val){
        return delegator.apply(val);
    }

    public<R,V1> U get(IFunction1<R,V1> val){
        return delegator.apply(val);
    }

    public<R,V1,V2> U get(IFunction2<R,V1,V2> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3> U get(IFunction3<R,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4> U get(IFunction4<R,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5> U get(IFunction5<R,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6> U get(IFunction6<R,V1,V2,V3,V4,V5,V6> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7> U get(IFunction7<R,V1,V2,V3,V4,V5,V6,V7> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8> U get(IFunction8<R,V1,V2,V3,V4,V5,V6,V7,V8> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> U get(IFunction9<R,V1,V2,V3,V4,V5,V6,V7,V8,V9> val){
        return delegator.apply(val);
    }

    public<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> U get(IFunction10<R,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U get(ICharFunction val){
        return delegator.apply(val);
    }

    public U get(ICharFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(ICharFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(ICharFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(ICharFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(ICharFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(ICharFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U get(IByteFunction val){
        return delegator.apply(val);
    }

    public U get(IByteFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IByteFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IByteFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IByteFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IByteFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IByteFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(IShortFunction val){
        return delegator.apply(val);
    }

    public U get(IShortFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IShortFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IShortFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IShortFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IShortFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IShortFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(ILongFunction val){
        return delegator.apply(val);
    }

    public U get(ILongFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(ILongFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(ILongFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(ILongFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(ILongFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(ILongFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(IFloatFunction val){
        return delegator.apply(val);
    }

    public U get(IFloatFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IFloatFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IFloatFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IFloatFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IFloatFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IFloatFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(IDoubleFunction val){
        return delegator.apply(val);
    }

    public U get(IDoubleFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IDoubleFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IDoubleFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IDoubleFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IDoubleFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IDoubleFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U get(IArrayFunction val){
        return delegator.apply(val);
    }



    public U get(IIntArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IIntArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IIntArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IIntArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IIntArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IIntArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IIntArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U get(IObjectArrayFunction val){
        return delegator.apply(val);
    }

    public<T> U get(IObjectArrayFunction0<T> val){
        return delegator.apply(val);
    }

    public<T,V1> U get(IObjectArrayFunction1<T,V1> val){
        return delegator.apply(val);
    }

    public<T,V1,V2> U get(IObjectArrayFunction2<T,V1,V2> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3> U get(IObjectArrayFunction3<T,V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4> U get(IObjectArrayFunction4<T,V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<T,V1,V2,V3,V4,V5> U get(IObjectArrayFunction5<T,V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public U get(ILongArrayFunction val){
        return delegator.apply(val);
    }

    public U get(ILongArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(ILongArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(ILongArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(ILongArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(ILongArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(ILongArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U get(IDoubleArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IDoubleArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IDoubleArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IDoubleArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IDoubleArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IDoubleArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IDoubleArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U get(ICharArrayFunction val){
        return delegator.apply(val);
    }

    public U get(ICharArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(ICharArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(ICharArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(ICharArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(ICharArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(ICharArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(IBoolArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IBoolArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IBoolArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IBoolArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IBoolArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IBoolArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IBoolArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////



    public U get(IShortArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IShortArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IShortArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IShortArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IShortArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IShortArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IShortArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////




    public U get(IByteArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IByteArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IByteArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IByteArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IByteArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IByteArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IByteArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////


    public U get(IFloatArrayFunction val){
        return delegator.apply(val);
    }

    public U get(IFloatArrayFunction0 val){
        return delegator.apply(val);
    }

    public<V1> U get(IFloatArrayFunction1<V1> val){
        return delegator.apply(val);
    }

    public<V1,V2> U get(IFloatArrayFunction2<V1,V2> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3> U get(IFloatArrayFunction3<V1,V2,V3> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4> U get(IFloatArrayFunction4<V1,V2,V3,V4> val){
        return delegator.apply(val);
    }

    public<V1,V2,V3,V4,V5> U get(IFloatArrayFunction5<V1,V2,V3,V4,V5> val){
        return delegator.apply(val);
    }

    //////////////////////////////////////////////////////////////////////////////////

}
