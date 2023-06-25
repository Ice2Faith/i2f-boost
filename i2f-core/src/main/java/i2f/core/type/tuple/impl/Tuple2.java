package i2f.core.type.tuple.impl;

import i2f.core.container.collection.Collections;
import i2f.core.type.tuple.ITuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tuple2<T1, T2> implements ITuple, Map.Entry<T1, T2> {
    public T1 t1;
    public T2 t2;

    @Override
    public List<Object> toList() {
        return Collections.arrayList(t1, t2);
    }

    @Override
    public T1 getKey() {
        return t1;
    }

    @Override
    public T2 getValue() {
        return t2;
    }

    @Override
    public T2 setValue(T2 value) {
        T2 ret = t2;
        t2 = value;
        return ret;
    }
}
