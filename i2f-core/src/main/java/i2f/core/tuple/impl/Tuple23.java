package i2f.core.tuple.impl;

import i2f.core.collection.Collections;
import i2f.core.tuple.ITuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> implements ITuple {
    public T1 t1;
    public T2 t2;
    public T3 t3;
    public T4 t4;
    public T5 t5;
    public T6 t6;
    public T7 t7;
    public T8 t8;
    public T9 t9;
    public T10 t10;
    public T11 t11;
    public T12 t12;
    public T13 t13;
    public T14 t14;
    public T15 t15;
    public T16 t16;
    public T17 t17;
    public T18 t18;
    public T19 t19;
    public T20 t20;
    public T21 t21;
    public T22 t22;
    public T23 t23;

    @Override
    public List<Object> toList() {
        return Collections.arrayList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23);
    }
}
