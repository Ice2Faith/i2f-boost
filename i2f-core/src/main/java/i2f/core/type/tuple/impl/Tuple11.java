package i2f.core.type.tuple.impl;

import i2f.core.container.collection.CollectionUtil;
import i2f.core.type.tuple.ITuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> implements ITuple {
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

    @Override
    public List<Object> toList() {
        return CollectionUtil.arrayList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }
}
