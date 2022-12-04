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
public class Tuple3<T1, T2, T3> implements ITuple {
    public T1 t1;
    public T2 t2;
    public T3 t3;

    @Override
    public List<Object> toList() {
        return Collections.arrayList(t1, t2, t3);
    }

}
