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
public class Tuple1<T1> implements ITuple {
    public T1 t1;

    @Override
    public List<Object> toList() {
        return CollectionUtil.arrayList(t1);
    }
}
