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
public class Tuple1<T1> implements ITuple {
    public T1 t1;

    @Override
    public List<Object> toList() {
        return Collections.arrayList(t1);
    }
}
