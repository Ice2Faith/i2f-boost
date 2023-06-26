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
public class Tuple6<T1, T2, T3, T4, T5, T6> implements ITuple {
    public T1 t1;
    public T2 t2;
    public T3 t3;
    public T4 t4;
    public T5 t5;
    public T6 t6;

    @Override
    public List<Object> toList() {
        return CollectionUtil.arrayList(t1, t2, t3, t4, t5, t6);
    }

}
